package com.yamone.korean.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yamone.korean.navigation.YamoneKoreanNavHost
import com.yamone.korean.ui.components.BannerAdSlot

@Composable
fun YamoneKoreanApp() {
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
                    YamoneKoreanNavHost(
                        navController = navController,
                    )
                }
            }
        }
    }
}
