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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.JamoCatalog
import com.yamone.korean.learning.JamoLesson

private data class JamoScreenText(
    val title: String,
    val intro: String,
    val consonants: String,
    val vowels: String,
    val sound: String,
    val example: String,
    val continueLabel: String,
)

@Composable
fun JamoScreen(
    languageCode: String?,
    onPractice: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val text = jamoScreenText(languageCode)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = text.title,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                modifier = Modifier.padding(top = 6.dp),
                text = text.intro,
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        item {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = text.consonants,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(
            items = JamoCatalog.basicConsonants,
            key = { it.id },
        ) { lesson ->
            JamoLessonCard(
                lesson = lesson,
                soundLabel = text.sound,
                exampleLabel = text.example,
                onPractice = onPractice,
            )
        }

        item {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = text.vowels,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        items(
            items = JamoCatalog.basicVowels,
            key = { it.id },
        ) { lesson ->
            JamoLessonCard(
                lesson = lesson,
                soundLabel = text.sound,
                exampleLabel = text.example,
                onPractice = onPractice,
            )
        }

        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                onClick = onContinue,
            ) {
                Text(text.continueLabel)
            }
        }
    }
}

@Composable
private fun JamoLessonCard(
    lesson: JamoLesson,
    soundLabel: String,
    exampleLabel: String,
    onPractice: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPractice(lesson.id) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = lesson.symbol,
                style = MaterialTheme.typography.displaySmall,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = soundLabel + ": " + lesson.soundGuide,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = exampleLabel + ": " + lesson.exampleSyllable,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Text("›", style = MaterialTheme.typography.titleLarge)
        }
    }
}

private fun jamoScreenText(languageCode: String?): JamoScreenText = when (languageCode) {
    "es" -> JamoScreenText(
        title = "Letras coreanas",
        intro = "Empieza por 14 consonantes y 10 vocales básicas. Mira la forma, relaciona el sonido y lee el ejemplo.",
        consonants = "Consonantes básicas",
        vowels = "Vocales básicas",
        sound = "Sonido",
        example = "Ejemplo",
        continueLabel = "Continuar con trazos",
    )
    "fr" -> JamoScreenText(
        title = "Lettres coréennes",
        intro = "Commence par 14 consonnes et 10 voyelles de base. Observe la forme, associe le son et lis l'exemple.",
        consonants = "Consonnes de base",
        vowels = "Voyelles de base",
        sound = "Son",
        example = "Exemple",
        continueLabel = "Continuer vers le tracé",
    )
    "vi" -> JamoScreenText(
        title = "Chữ cái tiếng Hàn",
        intro = "Bắt đầu với 14 phụ âm và 10 nguyên âm cơ bản. Nhìn hình, nối với âm và đọc ví dụ.",
        consonants = "Phụ âm cơ bản",
        vowels = "Nguyên âm cơ bản",
        sound = "Âm",
        example = "Ví dụ",
        continueLabel = "Tiếp tục luyện nét",
    )
    "th" -> JamoScreenText(
        title = "ตัวอักษรเกาหลี",
        intro = "เริ่มจากพยัญชนะพื้นฐาน 14 ตัวและสระพื้นฐาน 10 ตัว ดูรูป เชื่อมเสียง และอ่านตัวอย่าง",
        consonants = "พยัญชนะพื้นฐาน",
        vowels = "สระพื้นฐาน",
        sound = "เสียง",
        example = "ตัวอย่าง",
        continueLabel = "ฝึกลากเส้นต่อ",
    )
    "id" -> JamoScreenText(
        title = "Huruf Korea",
        intro = "Mulai dengan 14 konsonan dan 10 vokal dasar. Kenali bentuk, hubungkan bunyi, lalu baca contohnya.",
        consonants = "Konsonan dasar",
        vowels = "Vokal dasar",
        sound = "Bunyi",
        example = "Contoh",
        continueLabel = "Lanjut ke latihan goresan",
    )
    else -> JamoScreenText(
        title = "Korean letters",
        intro = "Start with 14 basic consonants and 10 basic vowels. See the shape, connect the sound, and read the example.",
        consonants = "Basic consonants",
        vowels = "Basic vowels",
        sound = "Sound",
        example = "Example",
        continueLabel = "Continue to tracing",
    )
}
