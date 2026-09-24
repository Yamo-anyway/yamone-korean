package com.yamone.korean.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.ui.components.BannerAdSlot

@Composable
fun YamoneKoreanApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                bottomBar = { BannerAdSlot() }
            ) { innerPadding ->
                HomePlaceholder(
                    contentPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun HomePlaceholder(
    contentPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Yamone Korean",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Learn Hangul from letters and touch writing to listening and speaking.",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Next: define the complete screen flow and learning navigation.",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
