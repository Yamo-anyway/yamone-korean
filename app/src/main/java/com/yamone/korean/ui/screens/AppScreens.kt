package com.yamone.korean.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.navigation.AppDestination
import com.yamone.korean.navigation.learningDestinations

private data class UiLanguage(
    val code: String,
    val label: String,
)

private val supportedLanguages = listOf(
    UiLanguage("en", "English"),
    UiLanguage("es", "Español"),
    UiLanguage("fr", "Français"),
    UiLanguage("vi", "Tiếng Việt"),
    UiLanguage("th", "ไทย"),
    UiLanguage("id", "Bahasa Indonesia"),
)

@Composable
fun LanguageSelectionScreen(
    onLanguageSelected: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Learn Korean",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                modifier = Modifier.padding(top = 6.dp, bottom = 12.dp),
                text = "Choose the language used for explanations.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        items(supportedLanguages) { language ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLanguageSelected(language.code) },
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = language.label,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    onOpenDestination: (AppDestination) -> Unit,
    onOpenSettings: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Text(
                text = "Yamone Korean",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                text = "Letters → writing → words → sentences → listening → speaking",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        items(learningDestinations) { destination ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenDestination(destination) },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = destination.title,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(text = "›")
                }
            }
        }

        item {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                onClick = onOpenSettings,
            ) {
                Text("Settings")
            }
        }
    }
}

@Composable
fun LearningStageScreen(
    destination: AppDestination,
    onNext: (() -> Unit)?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = destination.title,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = stageDescription(destination),
            style = MaterialTheme.typography.bodyLarge,
        )

        if (onNext != null) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = onNext,
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onChooseLanguage: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Progress, review history and preferences will be stored only on this device.",
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onChooseLanguage,
        ) {
            Text("Change explanation language")
        }
    }
}

private fun stageDescription(destination: AppDestination): String = when (destination) {
    AppDestination.Jamo -> "Recognize Korean consonants and vowels and connect each shape with its sound."
    AppDestination.Trace -> "Follow animated stroke order and trace each letter with your finger."
    AppDestination.Write -> "Write letters and syllables without tracing guides."
    AppDestination.Syllable -> "Combine initial consonants, vowels and final consonants into Hangul blocks."
    AppDestination.Word -> "Read and write high-frequency Korean words."
    AppDestination.Sentence -> "Build and understand short everyday Korean sentences."
    AppDestination.Listening -> "Listen at slow and natural speed and identify what was said."
    AppDestination.Speaking -> "Listen, repeat and practice producing your own Korean expressions."
    AppDestination.Review -> "Repeat letters, words and sentences that need more practice."
    else -> ""
}
