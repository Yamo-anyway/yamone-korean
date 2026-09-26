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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicWordCatalog
import com.yamone.korean.learning.BasicWordLesson
import com.yamone.korean.learning.WordCategory

private data class WordUiText(
    val title: String,
    val intro: String,
    val showMeaning: String,
    val learned: String,
    val learnedDone: String,
    val continueLabel: String,
)

@Composable
fun WordScreen(
    languageCode: String?,
    completedLessonIds: Set<String>,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val ui = wordUiText(languageCode)
    val revealed = remember { mutableStateMapOf<String, Boolean>() }

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
            items = BasicWordCatalog.lessons,
            key = { it.id },
        ) { lesson ->
            WordLessonCard(
                lesson = lesson,
                languageCode = languageCode,
                meaningRevealed = revealed[lesson.id] == true,
                completed = lesson.id in completedLessonIds,
                ui = ui,
                onReveal = {
                    revealed[lesson.id] = true
                    onLessonOpened(lesson.id)
                },
                onCompleted = {
                    revealed[lesson.id] = true
                    onLessonCompleted(lesson.id)
                },
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
private fun WordLessonCard(
    lesson: BasicWordLesson,
    languageCode: String?,
    meaningRevealed: Boolean,
    completed: Boolean,
    ui: WordUiText,
    onReveal: () -> Unit,
    onCompleted: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = categoryLabel(lesson.category, languageCode),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = lesson.korean,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = lesson.syllables,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (meaningRevealed || completed) {
                Text(
                    text = lesson.meaning(languageCode),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (!meaningRevealed && !completed) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onReveal,
                    ) {
                        Text(ui.showMeaning)
                    }
                } else {
                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = !completed,
                        onClick = onCompleted,
                    ) {
                        Text(if (completed) ui.learnedDone else ui.learned)
                    }
                }
            }
        }
    }
}

private fun categoryLabel(category: WordCategory, languageCode: String?): String {
    val labels = when (languageCode) {
        "es" -> mapOf(
            WordCategory.PEOPLE to "Personas",
            WordCategory.FOOD to "Comida",
            WordCategory.PLACE to "Lugares",
            WordCategory.TIME to "Tiempo",
            WordCategory.ACTION to "Acciones",
        )
        "fr" -> mapOf(
            WordCategory.PEOPLE to "Personnes",
            WordCategory.FOOD to "Nourriture",
            WordCategory.PLACE to "Lieux",
            WordCategory.TIME to "Temps",
            WordCategory.ACTION to "Actions",
        )
        "vi" -> mapOf(
            WordCategory.PEOPLE to "Con người",
            WordCategory.FOOD to "Đồ ăn",
            WordCategory.PLACE to "Địa điểm",
            WordCategory.TIME to "Thời gian",
            WordCategory.ACTION to "Hành động",
        )
        "th" -> mapOf(
            WordCategory.PEOPLE to "ผู้คน",
            WordCategory.FOOD to "อาหาร",
            WordCategory.PLACE to "สถานที่",
            WordCategory.TIME to "เวลา",
            WordCategory.ACTION to "การกระทำ",
        )
        "id" -> mapOf(
            WordCategory.PEOPLE to "Orang",
            WordCategory.FOOD to "Makanan",
            WordCategory.PLACE to "Tempat",
            WordCategory.TIME to "Waktu",
            WordCategory.ACTION to "Tindakan",
        )
        else -> mapOf(
            WordCategory.PEOPLE to "People",
            WordCategory.FOOD to "Food",
            WordCategory.PLACE to "Places",
            WordCategory.TIME to "Time",
            WordCategory.ACTION to "Actions",
        )
    }
    return labels.getValue(category)
}

private fun wordUiText(languageCode: String?): WordUiText = when (languageCode) {
    "es" -> WordUiText(
        title = "20 palabras esenciales",
        intro = "Lee primero el coreano. Separa las sílabas, intenta recordar el significado y luego compruébalo.",
        showMeaning = "Ver significado",
        learned = "Ya la sé",
        learnedDone = "Aprendida",
        continueLabel = "Ir a frases",
    )
    "fr" -> WordUiText(
        title = "20 mots essentiels",
        intro = "Lis d'abord le coréen. Observe les syllabes, devine le sens, puis vérifie.",
        showMeaning = "Voir le sens",
        learned = "Je connais",
        learnedDone = "Appris",
        continueLabel = "Passer aux phrases",
    )
    "vi" -> WordUiText(
        title = "20 từ thiết yếu",
        intro = "Đọc tiếng Hàn trước. Nhìn cách tách âm tiết, thử nhớ nghĩa rồi mới kiểm tra.",
        showMeaning = "Xem nghĩa",
        learned = "Tôi đã nhớ",
        learnedDone = "Đã học",
        continueLabel = "Sang câu",
    )
    "th" -> WordUiText(
        title = "คำพื้นฐาน 20 คำ",
        intro = "อ่านภาษาเกาหลีก่อน ดูการแบ่งพยางค์ ลองนึกความหมาย แล้วค่อยตรวจคำแปล",
        showMeaning = "ดูความหมาย",
        learned = "จำได้แล้ว",
        learnedDone = "เรียนแล้ว",
        continueLabel = "ไปเรียนประโยค",
    )
    "id" -> WordUiText(
        title = "20 kata penting",
        intro = "Baca bahasa Korea terlebih dahulu. Lihat pemisahan suku kata, tebak artinya, lalu periksa.",
        showMeaning = "Lihat arti",
        learned = "Saya sudah hafal",
        learnedDone = "Sudah dipelajari",
        continueLabel = "Lanjut ke kalimat",
    )
    else -> WordUiText(
        title = "20 essential Korean words",
        intro = "Read the Korean first. Notice the syllable blocks, recall the meaning, then reveal it.",
        showMeaning = "Show meaning",
        learned = "I know this",
        learnedDone = "Learned",
        continueLabel = "Continue to sentences",
    )
}
