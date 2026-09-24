package com.yamone.korean.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Fixed banner container reserved across learning screens.
 *
 * The first release intentionally has no rewarded-ad flow. A real banner SDK
 * can replace the placeholder content later without changing screen layouts.
 */
@Composable
fun BannerAdSlot() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        tonalElevation = 2.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "Banner ad",
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}
