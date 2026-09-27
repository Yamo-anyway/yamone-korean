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
import com.yamone.korean.ui.screens.ConversationScreen
import com.yamone.korean.ui.screens.HomeScreen
import com.yamone.korean.ui.screens.JamoScreen
import com.yamone.korean.ui.screens.LanguageSelectionScreen
import com.yamone.korean.ui.screens.LearningStageScreen
import com.yamone.korean.ui.screens.ListeningScreen
import com.yamone.korean.ui.screens.ReviewScreen
import com.yamone.korean.ui.screens.SelfExpressionScreen
import com.yamone.korean.ui.screens.SentenceScreen
import com.yamone.korean.ui.screens.SpeakingScreen
import com.yamone.korean.ui.screens.SettingsScreen
import com.yamone.korean.ui.screens.SyllableScreen
import com.yamone.korean.ui.screens.TraceScreen
import com.yamone.korean.ui.screens.WordScreen
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
    onLessonNeedsReview: suspend (String) -> Unit,
    onReviewResolved: suspend (String) -> Unit,
    onSyllableQuizAnswered: suspend (lessonId: String, isCorrect: Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    var requestedTraceLessonId by rememberSaveable { mutableStateOf<String?>(null) }
    var guidedSentenceId by rememberSaveable { mutableStateOf<String?>(null) }
    var reviewFocusLessonId by rememberSaveable { mutableStateOf<String?>(null) }
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
                languageCode = initialLanguageCode,
                learningProgress = learningProgress,
                onOpenDestination = { destination ->
                    scope.launch {
                        if (destination == AppDestination.Trace) {
                            requestedTraceLessonId = null
                        }
                        if (
                            destination == AppDestination.Sentence ||
                            destination == AppDestination.Listening ||
                            destination == AppDestination.Speaking
                        ) {
                            guidedSentenceId = null
                        }
                        if (
                            destination == AppDestination.Expression ||
                            destination == AppDestination.Conversation
                        ) {
                            reviewFocusLessonId = null
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
                            if (destination == AppDestination.Speaking) {
                                guidedSentenceId = null
                            }
                            if (
                                destination == AppDestination.Expression ||
                                destination == AppDestination.Conversation
                            ) {
                                reviewFocusLessonId = null
                            }
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

                    AppDestination.Word -> {
                        WordScreen(
                            languageCode = initialLanguageCode,
                            completedLessonIds = learningProgress.completedLessonIds,
                            onLessonOpened = { lessonId ->
                                scope.launch {
                                    onLessonOpened(AppDestination.Word.route, lessonId)
                                }
                            },
                            onLessonCompleted = { lessonId ->
                                scope.launch {
                                    onLessonCompleted(lessonId)
                                }
                            },
                            onStartGuidedPractice = { sentenceId ->
                                guidedSentenceId = sentenceId
                                scope.launch {
                                    onStageOpened(AppDestination.Sentence.route)
                                    onLessonOpened(AppDestination.Sentence.route, sentenceId)
                                    navController.navigate(AppDestination.Sentence.route)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Sentence -> {
                        SentenceScreen(
                            languageCode = initialLanguageCode,
                            completedLessonIds = learningProgress.completedLessonIds,
                            focusLessonId = guidedSentenceId,
                            onLessonOpened = { lessonId ->
                                scope.launch {
                                    onLessonOpened(AppDestination.Sentence.route, lessonId)
                                }
                            },
                            onLessonCompleted = { lessonId ->
                                scope.launch {
                                    onLessonCompleted(lessonId)
                                }
                            },
                            onLessonNeedsReview = { lessonId ->
                                scope.launch {
                                    onLessonNeedsReview(lessonId)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Listening -> {
                        ListeningScreen(
                            languageCode = initialLanguageCode,
                            completedLessonIds = learningProgress.completedLessonIds,
                            focusSentenceId = guidedSentenceId,
                            onLessonOpened = { lessonId ->
                                scope.launch {
                                    onLessonOpened(AppDestination.Listening.route, lessonId)
                                }
                            },
                            onLessonCompleted = { lessonId ->
                                scope.launch {
                                    onLessonCompleted(lessonId)
                                }
                            },
                            onLessonNeedsReview = { lessonId ->
                                scope.launch {
                                    onLessonNeedsReview(lessonId)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Speaking -> {
                        SpeakingScreen(
                            languageCode = initialLanguageCode,
                            completedLessonIds = learningProgress.completedLessonIds,
                            focusSentenceId = guidedSentenceId,
                            onLessonOpened = { lessonId ->
                                scope.launch {
                                    onLessonOpened(AppDestination.Speaking.route, lessonId)
                                }
                            },
                            onLessonCompleted = { lessonId ->
                                scope.launch {
                                    onLessonCompleted(lessonId)
                                }
                            },
                            onLessonNeedsReview = { lessonId ->
                                scope.launch {
                                    onLessonNeedsReview(lessonId)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Expression -> {
                        SelfExpressionScreen(
                            languageCode = initialLanguageCode,
                            completedLessonIds = learningProgress.completedLessonIds,
                            focusLessonId = reviewFocusLessonId,
                            onLessonOpened = { lessonId ->
                                scope.launch {
                                    onLessonOpened(AppDestination.Expression.route, lessonId)
                                }
                            },
                            onLessonCompleted = { lessonId ->
                                scope.launch {
                                    onLessonCompleted(lessonId)
                                }
                            },
                            onLessonNeedsReview = { lessonId ->
                                scope.launch {
                                    onLessonNeedsReview(lessonId)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Conversation -> {
                        ConversationScreen(
                            languageCode = initialLanguageCode,
                            completedLessonIds = learningProgress.completedLessonIds,
                            focusLessonId = reviewFocusLessonId,
                            onLessonOpened = { lessonId ->
                                scope.launch {
                                    onLessonOpened(AppDestination.Conversation.route, lessonId)
                                }
                            },
                            onLessonCompleted = { lessonId ->
                                scope.launch {
                                    onLessonCompleted(lessonId)
                                }
                            },
                            onLessonNeedsReview = { lessonId ->
                                scope.launch {
                                    onLessonNeedsReview(lessonId)
                                }
                            },
                            onContinue = onNext ?: {},
                        )
                    }

                    AppDestination.Review -> {
                        ReviewScreen(
                            languageCode = initialLanguageCode,
                            reviewLessonIds = learningProgress.reviewLessonIds,
                            onPracticeLesson = { lessonId ->
                                scope.launch {
                                    reviewFocusLessonId = null
                                    val target = when {
                                        lessonId.startsWith("listening_") -> {
                                            guidedSentenceId = lessonId.removePrefix("listening_")
                                            AppDestination.Listening
                                        }
                                        lessonId.startsWith("speaking_") -> {
                                            guidedSentenceId = lessonId.removePrefix("speaking_")
                                            AppDestination.Speaking
                                        }
                                        lessonId.startsWith("expression_") -> {
                                            reviewFocusLessonId = lessonId.removePrefix("expression_")
                                            AppDestination.Expression
                                        }
                                        lessonId.startsWith("conversation_") -> {
                                            reviewFocusLessonId = lessonId.removePrefix("conversation_")
                                            AppDestination.Conversation
                                        }
                                        lessonId.startsWith("word_") -> AppDestination.Word
                                        lessonId.startsWith("sentence_") -> {
                                            guidedSentenceId = lessonId
                                            AppDestination.Sentence
                                        }
                                        else -> AppDestination.Syllable
                                    }
                                    onStageOpened(target.route)
                                    onLessonOpened(target.route, lessonId)
                                    navController.navigate(target.route)
                                }
                            },
                            onReviewResolved = { lessonId ->
                                scope.launch {
                                    onReviewResolved(lessonId)
                                }
                            },
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
