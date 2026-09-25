package com.yamone.korean.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicSyllableCatalog

private data class SyllableQuizText(
    val title: String,
    val prompt: String,
    val chooseInitial: String,
    val chooseVowel: String,
    val check: String,
    val correct: String,
    val retry: String,
    val next: String,
)

@Composable
internal fun SyllableQuizCard(
    languageCode: String?,
    onAnswerChecked: (lessonId: String, isCorrect: Boolean) -> Unit,
) {
    val lessons = BasicSyllableCatalog.lessons
    val initialOptions = lessons.distinctBy { it.initialJamoId }
    val vowelOptions = lessons.distinctBy { it.vowelJamoId }
    val text = syllableQuizText(languageCode)

    var lessonIndex by rememberSaveable { mutableStateOf(0) }
    var selectedInitialId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedVowelId by rememberSaveable { mutableStateOf<String?>(null) }
    var checked by rememberSaveable { mutableStateOf(false) }

    val lesson = lessons[lessonIndex % lessons.size]
    val correct =
        selectedInitialId == lesson.initialJamoId &&
            selectedVowelId == lesson.vowelJamoId

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = text.title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = text.prompt,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = lesson.syllable,
                style = MaterialTheme.typography.displayMedium,
            )

            Text(
                text = text.chooseInitial,
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                initialOptions.forEach { option ->
                    val onSelect = {
                        selectedInitialId = option.initialJamoId
                        checked = false
                    }
                    if (selectedInitialId == option.initialJamoId) {
                        Button(onClick = onSelect) {
                            Text(option.initial)
                        }
                    } else {
                        OutlinedButton(onClick = onSelect) {
                            Text(option.initial)
                        }
                    }
                }
            }

            Text(
                text = text.chooseVowel,
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                vowelOptions.forEach { option ->
                    val onSelect = {
                        selectedVowelId = option.vowelJamoId
                        checked = false
                    }
                    if (selectedVowelId == option.vowelJamoId) {
                        Button(onClick = onSelect) {
                            Text(option.vowel)
                        }
                    } else {
                        OutlinedButton(onClick = onSelect) {
                            Text(option.vowel)
                        }
                    }
                }
            }

            if (!checked) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedInitialId != null && selectedVowelId != null,
                    onClick = {
                        checked = true
                        onAnswerChecked(lesson.id, correct)
                    },
                ) {
                    Text(text.check)
                }
            } else {
                Text(
                    text = if (correct) text.correct else text.retry,
                    style = MaterialTheme.typography.bodyLarge,
                )
                if (correct) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            lessonIndex = (lessonIndex + 1) % lessons.size
                            selectedInitialId = null
                            selectedVowelId = null
                            checked = false
                        },
                    ) {
                        Text(text.next)
                    }
                }
            }
        }
    }
}

private fun syllableQuizText(languageCode: String?): SyllableQuizText = when (languageCode) {
    "es" -> SyllableQuizText(
        title = "Prueba rápida de sílabas",
        prompt = "Mira la sílaba y elige la consonante inicial y la vocal correctas.",
        chooseInitial = "1. Elige una consonante inicial",
        chooseVowel = "2. Elige una vocal",
        check = "Comprobar respuesta",
        correct = "¡Correcto! Separaste bien la sílaba.",
        retry = "Aún no. Revisa las dos partes e inténtalo de nuevo.",
        next = "Siguiente pregunta",
    )
    "fr" -> SyllableQuizText(
        title = "Mini quiz de syllabes",
        prompt = "Regarde la syllabe puis choisis la bonne consonne initiale et la bonne voyelle.",
        chooseInitial = "1. Choisis une consonne initiale",
        chooseVowel = "2. Choisis une voyelle",
        check = "Vérifier la réponse",
        correct = "Correct ! Tu as bien séparé la syllabe.",
        retry = "Pas encore. Vérifie les deux parties puis réessaie.",
        next = "Question suivante",
    )
    "vi" -> SyllableQuizText(
        title = "Câu đố âm tiết nhanh",
        prompt = "Nhìn âm tiết rồi chọn đúng phụ âm đầu và nguyên âm.",
        chooseInitial = "1. Chọn phụ âm đầu",
        chooseVowel = "2. Chọn nguyên âm",
        check = "Kiểm tra đáp án",
        correct = "Đúng rồi! Bạn đã tách âm tiết chính xác.",
        retry = "Chưa đúng. Hãy kiểm tra hai phần rồi thử lại.",
        next = "Câu tiếp theo",
    )
    "th" -> SyllableQuizText(
        title = "แบบทดสอบพยางค์สั้น ๆ",
        prompt = "ดูพยางค์แล้วเลือกพยัญชนะต้นและสระที่ถูกต้อง",
        chooseInitial = "1. เลือกพยัญชนะต้น",
        chooseVowel = "2. เลือกสระ",
        check = "ตรวจคำตอบ",
        correct = "ถูกต้อง! คุณแยกพยางค์ได้ถูกต้อง",
        retry = "ยังไม่ถูก ตรวจทั้งสองส่วนแล้วลองอีกครั้ง",
        next = "ข้อต่อไป",
    )
    "id" -> SyllableQuizText(
        title = "Kuis suku kata singkat",
        prompt = "Lihat suku katanya, lalu pilih konsonan awal dan vokal yang benar.",
        chooseInitial = "1. Pilih konsonan awal",
        chooseVowel = "2. Pilih vokal",
        check = "Periksa jawaban",
        correct = "Benar! Kamu memisahkan suku katanya dengan tepat.",
        retry = "Belum tepat. Periksa kedua bagiannya lalu coba lagi.",
        next = "Soal berikutnya",
    )
    else -> SyllableQuizText(
        title = "Quick syllable quiz",
        prompt = "Look at the syllable, then choose the correct initial consonant and vowel.",
        chooseInitial = "1. Choose an initial consonant",
        chooseVowel = "2. Choose a vowel",
        check = "Check answer",
        correct = "Correct! You split the syllable correctly.",
        retry = "Not yet. Check both parts and try again.",
        next = "Next question",
    )
}
