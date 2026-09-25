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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicSyllableCatalog
import com.yamone.korean.learning.BasicSyllableLesson
import com.yamone.korean.learning.SyllableVowelPlacement

private data class SyllableScreenText(
    val title: String,
    val intro: String,
    val rightRule: String,
    val belowRule: String,
    val sound: String,
    val continueLabel: String,
)

@Composable
fun SyllableScreen(
    languageCode: String?,
    onContinue: () -> Unit,
) {
    val text = syllableScreenText(languageCode)
    var selectedLessonId by rememberSaveable { mutableStateOf(BasicSyllableCatalog.lessons.first().id) }
    val selectedLesson = BasicSyllableCatalog.byId(selectedLessonId) ?: BasicSyllableCatalog.lessons.first()
    val initialLessonIds = listOf("syllable_ga", "syllable_na", "syllable_mi", "syllable_bu")
    var selectedInitialLessonId by rememberSaveable { mutableStateOf(initialLessonIds.first()) }

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

        items(
            items = BasicSyllableCatalog.lessons,
            key = { it.id },
        ) { lesson ->
            SyllableCombinationCard(
                lesson = lesson,
                text = text,
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
private fun SyllableCombinationCard(
    lesson: BasicSyllableLesson,
    text: SyllableScreenText,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = lesson.initial + " + " + lesson.vowel + " → " + lesson.syllable,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = lesson.soundGuide,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Text(
                text = when (lesson.vowelPlacement) {
                    SyllableVowelPlacement.RIGHT -> text.rightRule
                    SyllableVowelPlacement.BELOW -> text.belowRule
                },
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = text.sound + ": " + lesson.soundGuide,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

private fun syllableScreenText(languageCode: String?): SyllableScreenText = when (languageCode) {
    "es" -> SyllableScreenText(
        title = "Combinar sílabas",
        intro = "Una sílaba coreana agrupa una consonante inicial y una vocal en un solo bloque. Primero practica bloques simples sin consonante final.",
        rightRule = "Con ㅏ, ㅓ, ㅑ, ㅕ o ㅣ, la vocal se coloca a la derecha de la consonante.",
        belowRule = "Con ㅗ, ㅜ, ㅛ, ㅠ o ㅡ, la vocal se coloca debajo de la consonante.",
        sound = "Sonido",
        continueLabel = "Continuar con palabras",
    )
    "fr" -> SyllableScreenText(
        title = "Combiner les syllabes",
        intro = "Une syllabe coréenne regroupe une consonne initiale et une voyelle dans un seul bloc. Commence par des blocs simples sans consonne finale.",
        rightRule = "Avec ㅏ, ㅓ, ㅑ, ㅕ ou ㅣ, la voyelle se place à droite de la consonne.",
        belowRule = "Avec ㅗ, ㅜ, ㅛ, ㅠ ou ㅡ, la voyelle se place sous la consonne.",
        sound = "Son",
        continueLabel = "Continuer vers les mots",
    )
    "vi" -> SyllableScreenText(
        title = "Ghép âm tiết",
        intro = "Một âm tiết tiếng Hàn ghép phụ âm đầu và nguyên âm thành một khối. Trước tiên hãy luyện các khối đơn giản chưa có phụ âm cuối.",
        rightRule = "Với ㅏ, ㅓ, ㅑ, ㅕ hoặc ㅣ, nguyên âm nằm bên phải phụ âm.",
        belowRule = "Với ㅗ, ㅜ, ㅛ, ㅠ hoặc ㅡ, nguyên âm nằm bên dưới phụ âm.",
        sound = "Âm",
        continueLabel = "Tiếp tục với từ",
    )
    "th" -> SyllableScreenText(
        title = "ผสมพยางค์",
        intro = "พยางค์เกาหลีรวมพยัญชนะต้นและสระไว้ในบล็อกเดียว เริ่มจากบล็อกง่าย ๆ ที่ยังไม่มีตัวสะกด",
        rightRule = "เมื่อใช้ ㅏ, ㅓ, ㅑ, ㅕ หรือ ㅣ สระจะอยู่ทางขวาของพยัญชนะ",
        belowRule = "เมื่อใช้ ㅗ, ㅜ, ㅛ, ㅠ หรือ ㅡ สระจะอยู่ใต้พยัญชนะ",
        sound = "เสียง",
        continueLabel = "เรียนคำต่อ",
    )
    "id" -> SyllableScreenText(
        title = "Menggabungkan suku kata",
        intro = "Satu suku kata Korea menggabungkan konsonan awal dan vokal menjadi satu blok. Mulailah dengan blok sederhana tanpa konsonan akhir.",
        rightRule = "Dengan ㅏ, ㅓ, ㅑ, ㅕ, atau ㅣ, vokal ditempatkan di sebelah kanan konsonan.",
        belowRule = "Dengan ㅗ, ㅜ, ㅛ, ㅠ, atau ㅡ, vokal ditempatkan di bawah konsonan.",
        sound = "Bunyi",
        continueLabel = "Lanjut ke kata",
    )
    else -> SyllableScreenText(
        title = "Build syllables",
        intro = "A Korean syllable combines an initial consonant and a vowel into one block. Start with simple blocks that do not have a final consonant.",
        rightRule = "With ㅏ, ㅓ, ㅑ, ㅕ or ㅣ, place the vowel to the right of the consonant.",
        belowRule = "With ㅗ, ㅜ, ㅛ, ㅠ or ㅡ, place the vowel below the consonant.",
        sound = "Sound",
        continueLabel = "Continue to words",
    )
}
