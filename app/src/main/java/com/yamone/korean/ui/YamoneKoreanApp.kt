package com.yamone.korean.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.yamone.korean.data.LanguagePreferenceState
import com.yamone.korean.data.LearningProgressRepository
import com.yamone.korean.data.LearningProgressState
import com.yamone.korean.data.UserPreferencesRepository
import com.yamone.korean.navigation.YamoneKoreanNavHost
import com.yamone.korean.ui.components.BannerAdSlot

@Composable
fun YamoneKoreanApp() {
    val context = LocalContext.current
    val preferencesRepository = remember(context) {
        UserPreferencesRepository(context.applicationContext)
    }
    val learningProgressRepository = remember(context) {
        LearningProgressRepository(context.applicationContext)
    }
    val languagePreference by preferencesRepository.languagePreference.collectAsState(
        initial = LanguagePreferenceState(),
    )
    val learningProgress by learningProgressRepository.progress.collectAsState(
        initial = LearningProgressState(),
    )

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()

            Scaffold(
                bottomBar = { BannerAdSlot() },
            ) { innerPadding ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    if (!languagePreference.isLoaded || !learningProgress.isLoaded) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        YamoneKoreanNavHost(
                            navController = navController,
                            initialLanguageCode = languagePreference.languageCode,
                            learningProgress = learningProgress,
                            onExplanationLanguageSelected = { languageCode ->
                                preferencesRepository.setExplanationLanguage(languageCode)
                            },
                            onStageOpened = { stageRoute ->
                                learningProgressRepository.setCurrentPosition(stageRoute)
                            },
                            onLessonOpened = { stageRoute, lessonId ->
                                learningProgressRepository.setCurrentPosition(stageRoute, lessonId)
                            },
                            onLessonCompleted = { lessonId ->
                                learningProgressRepository.markLessonCompleted(lessonId)
                            },
                        )
                    }
                }
            }
        }
    }
}
