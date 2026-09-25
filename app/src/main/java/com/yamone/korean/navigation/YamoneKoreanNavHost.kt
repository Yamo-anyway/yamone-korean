package com.yamone.korean.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yamone.korean.data.LearningProgressState
import com.yamone.korean.ui.screens.HomeScreen
import com.yamone.korean.ui.screens.JamoScreen
import com.yamone.korean.ui.screens.LanguageSelectionScreen
import com.yamone.korean.ui.screens.LearningStageScreen
import com.yamone.korean.ui.screens.SettingsScreen
import com.yamone.korean.ui.screens.SyllableScreen
import com.yamone.korean.ui.screens.TraceScreen
import kotlinx.coroutines.launch

@Composable
fun YamoneKoreanNavHost(
    navController: NavHostController,
    initialLanguageCode: String?,
    learningProgress: LearningProgressState,
    onExplanationLanguageSelected: suspend (String) -> Unit,
    onStageOpened: suspend (String) -> Unit,
    onLessonOpened: suspend (String, String) -> Unit,
    onLessonCompleted: suspend (String) -> Unit,
    onSyllableQuizAnswered: suspend (lessonId: String, isCorrect: Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var requestedTraceLessonId by rememberSaveable { mutableStateOf<String?>(null) }
    val startDestination = when {
        initialLanguageCode == null -> AppDestination.Language.route
        learningProgress.currentStageRoute == AppDestination.Trace.route &&
            learningProgress.currentLessonId != null -> AppDestination.Trace.route
        else -> AppDestination.Home.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(AppDestination.Language.route) {
            LanguageSelectionScreen(
                onLanguageSelected = { languageCode ->
                    val isFirstLanguageSelection = initialLanguageCode == null

                    scope.launch {
                        onExplanationLanguageSelected(languageCode)

                        if (isFirstLanguageSelection) {
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Language.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                },
            )
        }

        composable(AppDestination.Home.route) {
            HomeScreen(
                onOpenDestination = { destination ->
                    scope.launch {
                        if (destination == AppDestination.Trace) {
                            requestedTraceLessonId = null
                        }
                        onStageOpened(destination.route)
                        navController.navigate(destination.route)
                    }
                },
                onOpenSettings = {
                    navController.navigate(AppDestination.Settings.route)
                },
            )
        }

        learningDestinations.forEachIndexed { index, destination ->
            composable(destination.route) {
                val nextDestination = learningDestinations.getOrNull(index + 1)
                val onNext: (() -> Unit)? = nextDestination?.let { next ->
                    {
                        scope.launch {
                            onStageOpened(next.route)
                            navController.navigate(next.route)
                        }
                    }
                }

                when (destination) {
                    AppDestination.Jamo -> {
                        JamoScreen(
                            languageCode = initialLanguageCode,
                            onPractice = { lessonId ->
                                requestedTraceLessonId = lessonId
                                scope.launch {
                                    onLessonOpened(AppDestination.Trace.route, lessonId)
                                    navController.navigate(AppDestination.Trace.route)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Trace -> {
                        val savedTraceLessonId = learningProgress.currentLessonId
                            .takeIf { learningProgress.currentStageRoute == AppDestination.Trace.route }
                        val initialTraceLessonId = requestedTraceLessonId ?: savedTraceLessonId

                        TraceScreen(
                            initialLessonId = initialTraceLessonId,
                            completedLessonIds = learningProgress.completedLessonIds,
                            onLessonOpened = { lessonId ->
                                scope.launch {
                                    onLessonOpened(AppDestination.Trace.route, lessonId)
                                }
                            },
                            onLessonCompleted = { lessonId ->
                                scope.launch {
                                    onLessonCompleted(lessonId)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Syllable -> {
                        SyllableScreen(
                            languageCode = initialLanguageCode,
                            onQuizAnswered = { lessonId, isCorrect ->
                                scope.launch {
                                    onSyllableQuizAnswered(lessonId, isCorrect)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    else -> {
                        LearningStageScreen(
                            destination = destination,
                            onNext = onNext,
                        )
                    }
                }
            }
        }

        composable(AppDestination.Settings.route) {
            SettingsScreen(
                onChooseLanguage = {
                    navController.navigate(AppDestination.Language.route)
                },
            )
        }
    }
}
