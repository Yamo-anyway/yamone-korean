package com.yamone.korean.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yamone.korean.ui.screens.HomeScreen
import com.yamone.korean.ui.screens.LanguageSelectionScreen
import com.yamone.korean.ui.screens.LearningStageScreen
import com.yamone.korean.ui.screens.SettingsScreen

@Composable
fun YamoneKoreanNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Language.route,
    ) {
        composable(AppDestination.Language.route) {
            LanguageSelectionScreen(
                onLanguageSelected = {
                    navController.navigate(AppDestination.Home.route) {
                        popUpTo(AppDestination.Language.route) { inclusive = true }
                    }
                },
            )
        }

        composable(AppDestination.Home.route) {
            HomeScreen(
                onOpenDestination = { destination ->
                    navController.navigate(destination.route)
                },
                onOpenSettings = {
                    navController.navigate(AppDestination.Settings.route)
                },
            )
        }

        learningDestinations.forEachIndexed { index, destination ->
            composable(destination.route) {
                val nextDestination = learningDestinations.getOrNull(index + 1)
                LearningStageScreen(
                    destination = destination,
                    onNext = nextDestination?.let { next ->
                        { navController.navigate(next.route) }
                    },
                )
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
