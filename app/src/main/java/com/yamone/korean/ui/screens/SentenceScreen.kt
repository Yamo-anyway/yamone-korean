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
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val ui = sentenceUiText(languageCode)

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
            items = BasicSentenceCatalog.lessons,
            key = { it.id },
        ) { lesson ->
            SentenceLessonCard(
                lesson = lesson,
                languageCode = languageCode,
                completed = lesson.id in completedLessonIds,
                ui = ui,
                onOpened = { onLessonOpened(lesson.id) },
                onCompleted = { onLessonCompleted(lesson.id) },
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
                            if (isCorrect) onCompleted()
                        }
                    },
                ) {
                    Text(chunk)
                }
            }

            Text(
                text = "${ui.selectedLabel}: " +
                    selectedChunks.joinToString(" ").ifBlank { "—" },
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
        intro = "Aprende el orden real de las palabras y luego reconstruye cada frase. En coreano, la acción suele ir al final.",
        patternLabel = "Patrón",
        meaningLabel = "Significado",
        noteLabel = "Cómo funciona",
        buildLabel = "Construye la frase en orden",
        selectedLabel = "Tu frase",
        resetLabel = "Reiniciar",
        correctLabel = "¡Correcto! Ya puedes usar este patrón.",
        retryLabel = "El orden aún no es correcto. Inténtalo de nuevo.",
        completedLabel = "Patrón aprendido",
        continueLabel = "Continuar a escucha",
    )

    "fr" -> SentenceUiText(
        title = "Premières phrases en coréen",
        intro = "Apprends l'ordre réel des mots puis reconstruis chaque phrase. En coréen, l'action se place généralement à la fin.",
        patternLabel = "Modèle",
        meaningLabel = "Sens",
        noteLabel = "Fonctionnement",
        buildLabel = "Reconstruis la phrase dans l'ordre",
        selectedLabel = "Ta phrase",
        resetLabel = "Recommencer",
        correctLabel = "Correct ! Tu peux réutiliser ce modèle.",
        retryLabel = "L'ordre n'est pas encore correct. Réessaie.",
        completedLabel = "Modèle appris",
        continueLabel = "Continuer vers l'écoute",
    )

    "vi" -> SentenceUiText(
        title = "Những câu tiếng Hàn đầu tiên",
        intro = "Học trật tự từ thực tế rồi tự ghép lại từng câu. Trong tiếng Hàn, động từ thường đứng cuối.",
        patternLabel = "Mẫu câu",
        meaningLabel = "Nghĩa",
        noteLabel = "Cách dùng",
        buildLabel = "Ghép câu theo đúng thứ tự",
        selectedLabel = "Câu của bạn",
        resetLabel = "Làm lại",
        correctLabel = "Đúng rồi! Bạn có thể dùng lại mẫu câu này.",
        retryLabel = "Thứ tự chưa đúng. Hãy thử lại.",
        completedLabel = "Đã học mẫu câu",
        continueLabel = "Tiếp tục sang nghe",
    )

    "th" -> SentenceUiText(
        title = "ประโยคภาษาเกาหลีชุดแรก",
        intro = "เรียนรู้ลำดับคำที่ใช้จริง แล้วเรียงประโยคด้วยตัวเอง ภาษาเกาหลีมักวางคำกริยาไว้ท้ายประโยค",
        patternLabel = "รูปแบบ",
        meaningLabel = "ความหมาย",
        noteLabel = "วิธีใช้",
        buildLabel = "เรียงประโยคให้ถูกต้อง",
        selectedLabel = "ประโยคของคุณ",
        resetLabel = "เริ่มใหม่",
        correctLabel = "ถูกต้อง! ใช้รูปแบบนี้สร้างประโยคใหม่ได้แล้ว",
        retryLabel = "ลำดับยังไม่ถูก ลองอีกครั้ง",
        completedLabel = "เรียนรูปแบบนี้แล้ว",
        continueLabel = "ไปฝึกฟัง",
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
