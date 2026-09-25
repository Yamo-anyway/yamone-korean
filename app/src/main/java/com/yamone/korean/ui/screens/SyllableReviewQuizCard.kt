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

private data class SyllableReviewQuizText(
    val title: String,
    val empty: String,
    val prompt: String,
    val chooseInitial: String,
    val chooseVowel: String,
    val check: String,
    val correct: String,
    val retry: String,
    val resolve: String,
)

@Composable
internal fun SyllableReviewQuizCard(
    languageCode: String?,
    reviewLessonIds: Set<String>,
    onReviewResolved: (lessonId: String) -> Unit,
) {
    val text = syllableReviewQuizText(languageCode)
    val reviewLessons = BasicSyllableCatalog.lessons.filter { it.id in reviewLessonIds }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = text.title,
                style = MaterialTheme.typography.titleLarge,
            )

            if (reviewLessons.isEmpty()) {
                Text(
                    text = text.empty,
                    style = MaterialTheme.typography.bodyLarge,
                )
                return@Column
            }

            val initialOptions = BasicSyllableCatalog.lessons.distinctBy { it.initialJamoId }
            val vowelOptions = BasicSyllableCatalog.lessons.distinctBy { it.vowelJamoId }

            var lessonIndex by rememberSaveable { mutableStateOf(0) }
            var selectedInitialId by rememberSaveable { mutableStateOf<String?>(null) }
            var selectedVowelId by rememberSaveable { mutableStateOf<String?>(null) }
            var checked by rememberSaveable { mutableStateOf(false) }

            val lesson = reviewLessons[lessonIndex % reviewLessons.size]
            val correct =
                selectedInitialId == lesson.initialJamoId &&
                    selectedVowelId == lesson.vowelJamoId

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
                    onClick = { checked = true },
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
                            onReviewResolved(lesson.id)
                            lessonIndex = 0
                            selectedInitialId = null
                            selectedVowelId = null
                            checked = false
                        },
                    ) {
                        Text(text.resolve)
                    }
                }
            }
        }
    }
}

private fun syllableReviewQuizText(languageCode: String?): SyllableReviewQuizText = when (languageCode) {
    "es" -> SyllableReviewQuizText(
        title = "Repasar errores",
        empty = "No hay sílabas pendientes de repaso.",
        prompt = "Vuelve a construir una sílaba que respondiste mal.",
        chooseInitial = "1. Elige la consonante inicial",
        chooseVowel = "2. Elige la vocal",
        check = "Comprobar respuesta",
        correct = "¡Correcto! Ya puedes retirar esta sílaba del repaso.",
        retry = "Aún no. Inténtalo otra vez.",
        resolve = "Marcar como repasada",
    )
    "fr" -> SyllableReviewQuizText(
        title = "Revoir les erreurs",
        empty = "Aucune syllabe n'attend une révision.",
        prompt = "Reconstruis une syllabe que tu avais mal répondue.",
        chooseInitial = "1. Choisis la consonne initiale",
        chooseVowel = "2. Choisis la voyelle",
        check = "Vérifier la réponse",
        correct = "Correct ! Tu peux retirer cette syllabe de la révision.",
        retry = "Pas encore. Réessaie.",
        resolve = "Marquer comme révisée",
    )
    "vi" -> SyllableReviewQuizText(
        title = "Ôn lại câu sai",
        empty = "Không có âm tiết nào đang chờ ôn tập.",
        prompt = "Ghép lại một âm tiết mà bạn đã trả lời sai.",
        chooseInitial = "1. Chọn phụ âm đầu",
        chooseVowel = "2. Chọn nguyên âm",
        check = "Kiểm tra đáp án",
        correct = "Đúng rồi! Bạn có thể xóa âm tiết này khỏi danh sách ôn tập.",
        retry = "Chưa đúng. Hãy thử lại.",
        resolve = "Đánh dấu đã ôn",
    )
    "th" -> SyllableReviewQuizText(
        title = "ทบทวนข้อที่ผิด",
        empty = "ไม่มีพยางค์ที่รอทบทวน",
        prompt = "ประกอบพยางค์ที่เคยตอบผิดอีกครั้ง",
        chooseInitial = "1. เลือกพยัญชนะต้น",
        chooseVowel = "2. เลือกสระ",
        check = "ตรวจคำตอบ",
        correct = "ถูกต้อง! นำพยางค์นี้ออกจากรายการทบทวนได้แล้ว",
        retry = "ยังไม่ถูก ลองอีกครั้ง",
        resolve = "ทำเครื่องหมายว่าทบทวนแล้ว",
    )
    "id" -> SyllableReviewQuizText(
        title = "Ulangi jawaban salah",
        empty = "Tidak ada suku kata yang perlu diulang.",
        prompt = "Susun kembali suku kata yang sebelumnya kamu jawab salah.",
        chooseInitial = "1. Pilih konsonan awal",
        chooseVowel = "2. Pilih vokal",
        check = "Periksa jawaban",
        correct = "Benar! Suku kata ini bisa dihapus dari daftar pengulangan.",
        retry = "Belum tepat. Coba lagi.",
        resolve = "Tandai sudah diulang",
    )
    else -> SyllableReviewQuizText(
        title = "Review mistakes",
        empty = "No syllables are waiting for review.",
        prompt = "Build a syllable that you answered incorrectly before.",
        chooseInitial = "1. Choose an initial consonant",
        chooseVowel = "2. Choose a vowel",
        check = "Check answer",
        correct = "Correct! You can remove this syllable from review.",
        retry = "Not yet. Try again.",
        resolve = "Mark as reviewed",
    )
}
