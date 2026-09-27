package com.yamone.korean.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicSentenceCatalog
import com.yamone.korean.learning.BasicSentenceLesson

private data class SentenceUiText(
    val title: String,
    val intro: String,
    val patternLabel: String,
    val meaningLabel: String,
    val noteLabel: String,
    val buildLabel: String,
    val selectedLabel: String,
    val resetLabel: String,
    val correctLabel: String,
    val retryLabel: String,
    val completedLabel: String,
    val continueLabel: String,
)

@Composable
fun SentenceScreen(
    languageCode: String?,
    completedLessonIds: Set<String>,
    focusLessonId: String? = null,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onLessonNeedsReview: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val ui = sentenceUiText(languageCode)
    val orderedLessons = remember(focusLessonId) {
        if (focusLessonId == null) {
            BasicSentenceCatalog.lessons
        } else {
            BasicSentenceCatalog.lessons.sortedBy { lesson ->
                if (lesson.id == focusLessonId) 0 else 1
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = ui.title,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                modifier = Modifier.padding(top = 6.dp, bottom = 8.dp),
                text = ui.intro,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        items(
            items = orderedLessons,
            key = { it.id },
        ) { lesson ->
            SentenceLessonCard(
                lesson = lesson,
                languageCode = languageCode,
                completed = lesson.id in completedLessonIds,
                ui = ui,
                onOpened = { onLessonOpened(lesson.id) },
                onCompleted = { onLessonCompleted(lesson.id) },
                onNeedsReview = { onLessonNeedsReview(lesson.id) },
            )
        }

        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                onClick = onContinue,
            ) {
                Text(ui.continueLabel)
            }
        }
    }
}

@Composable
private fun SentenceLessonCard(
    lesson: BasicSentenceLesson,
    languageCode: String?,
    completed: Boolean,
    ui: SentenceUiText,
    onOpened: () -> Unit,
    onCompleted: () -> Unit,
    onNeedsReview: () -> Unit,
) {
    var selectedChunks by remember(lesson.id) { mutableStateOf(emptyList<String>()) }
    var result by remember(lesson.id) { mutableStateOf<Boolean?>(null) }
    var opened by remember(lesson.id) { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "${ui.patternLabel}: ${lesson.pattern}",
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = lesson.korean,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "${ui.meaningLabel}: ${lesson.meaning(languageCode)}",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "${ui.noteLabel}: ${lesson.grammarNote(languageCode)}",
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = ui.buildLabel,
                style = MaterialTheme.typography.titleSmall,
            )

            lesson.choiceChunks.forEach { chunk ->
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = result == null && chunk !in selectedChunks,
                    onClick = {
                        if (!opened) {
                            opened = true
                            onOpened()
                        }

                        val nextSelection = selectedChunks + chunk
                        selectedChunks = nextSelection

                        if (nextSelection.size == lesson.correctChunks.size) {
                            val isCorrect = nextSelection == lesson.correctChunks
                            result = isCorrect
                            if (isCorrect) {
                                onCompleted()
                            } else {
                                onNeedsReview()
                            }
                        }
                    },
                ) {
                    Text(chunk)
                }
            }

            Text(
                text = "${ui.selectedLabel}: " +
                    selectedChunks.joinToString(" ").ifBlank { "\u2014" },
                style = MaterialTheme.typography.bodyLarge,
            )

            when (result) {
                true -> Text(
                    text = ui.correctLabel,
                    style = MaterialTheme.typography.titleMedium,
                )

                false -> Text(
                    text = ui.retryLabel,
                    style = MaterialTheme.typography.titleMedium,
                )

                null -> if (completed) {
                    Text(
                        text = ui.completedLabel,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            if (selectedChunks.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    OutlinedButton(
                        onClick = {
                            selectedChunks = emptyList()
                            result = null
                        },
                    ) {
                        Text(ui.resetLabel)
                    }
                }
            }
        }
    }
}

private fun sentenceUiText(languageCode: String?): SentenceUiText = when (languageCode) {
    "es" -> SentenceUiText(
        title = "Primeras frases en coreano",
        intro = "Aprende el orden real de las palabras y luego reconstruye cada frase. En coreano, la acci\u00f3n suele ir al final.",
        patternLabel = "Patr\u00f3n",
        meaningLabel = "Significado",
        noteLabel = "C\u00f3mo funciona",
        buildLabel = "Construye la frase en orden",
        selectedLabel = "Tu frase",
        resetLabel = "Reiniciar",
        correctLabel = "\u00a1Correcto! Ya puedes usar este patr\u00f3n.",
        retryLabel = "El orden a\u00fan no es correcto. Int\u00e9ntalo de nuevo.",
        completedLabel = "Patr\u00f3n aprendido",
        continueLabel = "Continuar a escucha",
    )

    "fr" -> SentenceUiText(
        title = "Premi\u00e8res phrases en cor\u00e9en",
        intro = "Apprends l'ordre r\u00e9el des mots puis reconstruis chaque phrase. En cor\u00e9en, l'action se place g\u00e9n\u00e9ralement \u00e0 la fin.",
        patternLabel = "Mod\u00e8le",
        meaningLabel = "Sens",
        noteLabel = "Fonctionnement",
        buildLabel = "Reconstruis la phrase dans l'ordre",
        selectedLabel = "Ta phrase",
        resetLabel = "Recommencer",
        correctLabel = "Correct ! Tu peux r\u00e9utiliser ce mod\u00e8le.",
        retryLabel = "L'ordre n'est pas encore correct. R\u00e9essaie.",
        completedLabel = "Mod\u00e8le appris",
        continueLabel = "Continuer vers l'\u00e9coute",
    )

    "vi" -> SentenceUiText(
        title = "Nh\u1eefng c\u00e2u ti\u1ebfng H\u00e0n \u0111\u1ea7u ti\u00ean",
        intro = "H\u1ecdc tr\u1eadt t\u1ef1 t\u1eeb th\u1ef1c t\u1ebf r\u1ed3i t\u1ef1 gh\u00e9p l\u1ea1i t\u1eebng c\u00e2u. Trong ti\u1ebfng H\u00e0n, \u0111\u1ed9ng t\u1eeb th\u01b0\u1eddng \u0111\u1ee9ng cu\u1ed1i.",
        patternLabel = "M\u1eabu c\u00e2u",
        meaningLabel = "Ngh\u0129a",
        noteLabel = "C\u00e1ch d\u00f9ng",
        buildLabel = "Gh\u00e9p c\u00e2u theo \u0111\u00fang th\u1ee9 t\u1ef1",
        selectedLabel = "C\u00e2u c\u1ee7a b\u1ea1n",
        resetLabel = "L\u00e0m l\u1ea1i",
        correctLabel = "\u0110\u00fang r\u1ed3i! B\u1ea1n c\u00f3 th\u1ec3 d\u00f9ng l\u1ea1i m\u1eabu c\u00e2u n\u00e0y.",
        retryLabel = "Th\u1ee9 t\u1ef1 ch\u01b0a \u0111\u00fang. H\u00e3y th\u1eed l\u1ea1i.",
        completedLabel = "\u0110\u00e3 h\u1ecdc m\u1eabu c\u00e2u",
        continueLabel = "Ti\u1ebfp t\u1ee5c sang nghe",
    )

    "th" -> SentenceUiText(
        title = "\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e0a\u0e38\u0e14\u0e41\u0e23\u0e01",
        intro = "\u0e40\u0e23\u0e35\u0e22\u0e19\u0e23\u0e39\u0e49\u0e25\u0e33\u0e14\u0e31\u0e1a\u0e04\u0e33\u0e17\u0e35\u0e48\u0e43\u0e0a\u0e49\u0e08\u0e23\u0e34\u0e07 \u0e41\u0e25\u0e49\u0e27\u0e40\u0e23\u0e35\u0e22\u0e07\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e14\u0e49\u0e27\u0e22\u0e15\u0e31\u0e27\u0e40\u0e2d\u0e07 \u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e21\u0e31\u0e01\u0e27\u0e32\u0e07\u0e04\u0e33\u0e01\u0e23\u0e34\u0e22\u0e32\u0e44\u0e27\u0e49\u0e17\u0e49\u0e32\u0e22\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04",
        patternLabel = "\u0e23\u0e39\u0e1b\u0e41\u0e1a\u0e1a",
        meaningLabel = "\u0e04\u0e27\u0e32\u0e21\u0e2b\u0e21\u0e32\u0e22",
        noteLabel = "\u0e27\u0e34\u0e18\u0e35\u0e43\u0e0a\u0e49",
        buildLabel = "\u0e40\u0e23\u0e35\u0e22\u0e07\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e43\u0e2b\u0e49\u0e16\u0e39\u0e01\u0e15\u0e49\u0e2d\u0e07",
        selectedLabel = "\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e02\u0e2d\u0e07\u0e04\u0e38\u0e13",
        resetLabel = "\u0e40\u0e23\u0e34\u0e48\u0e21\u0e43\u0e2b\u0e21\u0e48",
        correctLabel = "\u0e16\u0e39\u0e01\u0e15\u0e49\u0e2d\u0e07! \u0e43\u0e0a\u0e49\u0e23\u0e39\u0e1b\u0e41\u0e1a\u0e1a\u0e19\u0e35\u0e49\u0e2a\u0e23\u0e49\u0e32\u0e07\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e43\u0e2b\u0e21\u0e48\u0e44\u0e14\u0e49\u0e41\u0e25\u0e49\u0e27",
        retryLabel = "\u0e25\u0e33\u0e14\u0e31\u0e1a\u0e22\u0e31\u0e07\u0e44\u0e21\u0e48\u0e16\u0e39\u0e01 \u0e25\u0e2d\u0e07\u0e2d\u0e35\u0e01\u0e04\u0e23\u0e31\u0e49\u0e07",
        completedLabel = "\u0e40\u0e23\u0e35\u0e22\u0e19\u0e23\u0e39\u0e1b\u0e41\u0e1a\u0e1a\u0e19\u0e35\u0e49\u0e41\u0e25\u0e49\u0e27",
        continueLabel = "\u0e44\u0e1b\u0e1d\u0e36\u0e01\u0e1f\u0e31\u0e07",
    )

    "id" -> SentenceUiText(
        title = "Kalimat Korea pertama",
        intro = "Pelajari urutan kata yang benar lalu susun kembali setiap kalimat. Dalam bahasa Korea, kata kerja biasanya berada di akhir.",
        patternLabel = "Pola",
        meaningLabel = "Arti",
        noteLabel = "Cara kerja",
        buildLabel = "Susun kalimat dengan urutan yang benar",
        selectedLabel = "Kalimat Anda",
        resetLabel = "Ulangi",
        correctLabel = "Benar! Pola ini sudah bisa Anda gunakan.",
        retryLabel = "Urutannya belum benar. Coba lagi.",
        completedLabel = "Pola sudah dipelajari",
        continueLabel = "Lanjut ke mendengarkan",
    )

    else -> SentenceUiText(
        title = "Your first Korean sentences",
        intro = "Learn real Korean word order, then rebuild each sentence yourself. Korean usually puts the action at the end.",
        patternLabel = "Pattern",
        meaningLabel = "Meaning",
        noteLabel = "How it works",
        buildLabel = "Build the sentence in the correct order",
        selectedLabel = "Your sentence",
        resetLabel = "Reset",
        correctLabel = "Correct! You can reuse this pattern.",
        retryLabel = "The order is not correct yet. Try again.",
        completedLabel = "Pattern learned",
        continueLabel = "Continue to listening",
    )
}
