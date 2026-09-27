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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicSentenceCatalog
import com.yamone.korean.learning.BasicSyllableCatalog
import com.yamone.korean.learning.BasicWordCatalog
import com.yamone.korean.learning.ConversationCatalog
import com.yamone.korean.learning.SelfExpressionCatalog

private enum class ReviewCategory {
    READING,
    WORD,
    SENTENCE,
    LISTENING,
    SPEAKING,
    EXPRESSION,
    CONVERSATION,
    OTHER,
}

private data class ReviewUiText(
    val title: String,
    val intro: String,
    val empty: String,
    val practiceAgain: String,
    val resolved: String,
    val categories: Map<ReviewCategory, String>,
)

@Composable
fun ReviewScreen(
    languageCode: String?,
    reviewLessonIds: Set<String>,
    onPracticeLesson: (String) -> Unit,
    onReviewResolved: (String) -> Unit,
) {
    val ui = reviewUiText(languageCode)
    val grouped = reviewLessonIds
        .sorted()
        .groupBy(::reviewCategory)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Text(
                text = ui.title,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = ui.intro,
                modifier = Modifier.padding(top = 6.dp, bottom = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        if (reviewLessonIds.isEmpty()) {
            item {
                Card(Modifier.fillMaxWidth()) {
                    Text(
                        text = ui.empty,
                        modifier = Modifier.padding(18.dp),
                    )
                }
            }
        } else {
            ReviewCategory.values().forEach { category ->
                val ids = grouped[category].orEmpty()
                if (ids.isNotEmpty()) {
                    item(key = "header_${category.name}") {
                        Text(
                            text = "${ui.categories.getValue(category)} · ${ids.size}",
                            modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    items(
                        items = ids,
                        key = { it },
                    ) { id ->
                        val korean = reviewKorean(id)
                        val meaning = reviewMeaning(id, languageCode)

                        Card(Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = korean,
                                    style = MaterialTheme.typography.headlineSmall,
                                )

                                if (meaning.isNotBlank()) {
                                    Text(
                                        text = meaning,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Button(
                                        modifier = Modifier.weight(1f),
                                        onClick = { onPracticeLesson(id) },
                                    ) {
                                        Text(ui.practiceAgain)
                                    }
                                    OutlinedButton(
                                        modifier = Modifier.weight(1f),
                                        onClick = { onReviewResolved(id) },
                                    ) {
                                        Text(ui.resolved)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun reviewCategory(id: String): ReviewCategory = when {
    id.startsWith("listening_") -> ReviewCategory.LISTENING
    id.startsWith("speaking_") -> ReviewCategory.SPEAKING
    id.startsWith("expression_") -> ReviewCategory.EXPRESSION
    id.startsWith("conversation_") -> ReviewCategory.CONVERSATION
    id.startsWith("word_") -> ReviewCategory.WORD
    id.startsWith("sentence_") -> ReviewCategory.SENTENCE
    BasicSyllableCatalog.byId(id) != null -> ReviewCategory.READING
    else -> ReviewCategory.OTHER
}

private fun reviewKorean(id: String): String {
    BasicSyllableCatalog.byId(id)?.let {
        return "${it.syllable}  (${it.initial} + ${it.vowel})"
    }

    BasicWordCatalog.lessons.firstOrNull { it.id == id }?.let {
        return it.korean
    }

    BasicSentenceCatalog.lessons.firstOrNull { it.id == id }?.let {
        return it.korean
    }

    listOf("listening_", "speaking_").firstOrNull { id.startsWith(it) }?.let { prefix ->
        BasicSentenceCatalog.lessons
            .firstOrNull { it.id == id.removePrefix(prefix) }
            ?.let { return it.korean }
    }

    if (id.startsWith("expression_")) {
        SelfExpressionCatalog.lessons
            .firstOrNull { it.id == id.removePrefix("expression_") }
            ?.let { lesson ->
                return lesson.choices.firstOrNull()?.sentence ?: lesson.template
            }
    }

    if (id.startsWith("conversation_")) {
        ConversationCatalog.lessons
            .firstOrNull { it.id == id.removePrefix("conversation_") }
            ?.let { return it.partnerLine }
    }

    return id
}

private fun reviewMeaning(id: String, languageCode: String?): String {
    BasicWordCatalog.lessons.firstOrNull { it.id == id }?.let {
        return it.meaning(languageCode)
    }

    BasicSentenceCatalog.lessons.firstOrNull { it.id == id }?.let {
        return it.meaning(languageCode)
    }

    listOf("listening_", "speaking_").firstOrNull { id.startsWith(it) }?.let { prefix ->
        BasicSentenceCatalog.lessons
            .firstOrNull { it.id == id.removePrefix(prefix) }
            ?.let { return it.meaning(languageCode) }
    }

    if (id.startsWith("expression_")) {
        SelfExpressionCatalog.lessons
            .firstOrNull { it.id == id.removePrefix("expression_") }
            ?.let { return it.prompt(languageCode) }
    }

    if (id.startsWith("conversation_")) {
        ConversationCatalog.lessons
            .firstOrNull { it.id == id.removePrefix("conversation_") }
            ?.let { return it.partnerMeaning(languageCode) }
    }

    return ""
}

private fun reviewUiText(languageCode: String?): ReviewUiText {
    fun categories(
        reading: String,
        word: String,
        sentence: String,
        listening: String,
        speaking: String,
        expression: String,
        conversation: String,
        other: String,
    ) = mapOf(
        ReviewCategory.READING to reading,
        ReviewCategory.WORD to word,
        ReviewCategory.SENTENCE to sentence,
        ReviewCategory.LISTENING to listening,
        ReviewCategory.SPEAKING to speaking,
        ReviewCategory.EXPRESSION to expression,
        ReviewCategory.CONVERSATION to conversation,
        ReviewCategory.OTHER to other,
    )

    return when (languageCode) {
        "es" -> ReviewUiText(
            title = "Repaso",
            intro = "Repite por tipo lo que fue difícil y vuelve directamente a esa práctica.",
            empty = "No hay nada pendiente.",
            practiceAgain = "Practicar",
            resolved = "Ya lo sé",
            categories = categories(
                "Lectura", "Palabras", "Frases", "Escucha", "Habla",
                "Mi expresión", "Conversación", "Otros",
            ),
        )

        "fr" -> ReviewUiText(
            title = "Révision",
            intro = "Repratique par type ce qui était difficile et retourne directement à l'exercice.",
            empty = "Rien à réviser.",
            practiceAgain = "Repratiquer",
            resolved = "Je le maîtrise",
            categories = categories(
                "Lecture", "Mots", "Phrases", "Écoute", "Expression orale",
                "Mes phrases", "Conversation", "Autres",
            ),
        )

        "vi" -> ReviewUiText(
            title = "Ôn tập",
            intro = "Ôn lại theo từng loại và quay thẳng về bài luyện cần thiết.",
            empty = "Không có mục cần ôn.",
            practiceAgain = "Luyện lại",
            resolved = "Tôi đã nhớ",
            categories = categories(
                "Đọc", "Từ vựng", "Câu", "Nghe", "Nói",
                "Câu của tôi", "Hội thoại", "Khác",
            ),
        )

        "th" -> ReviewUiText(
            title = "ทบทวน",
            intro = "ทบทวนสิ่งที่ยากตามประเภท แล้วกลับไปฝึกส่วนนั้นได้ทันที",
            empty = "ไม่มีรายการที่ต้องทบทวน",
            practiceAgain = "ฝึกอีกครั้ง",
            resolved = "จำได้แล้ว",
            categories = categories(
                "การอ่าน", "คำศัพท์", "ประโยค", "การฟัง", "การพูด",
                "ประโยคของฉัน", "บทสนทนา", "อื่น ๆ",
            ),
        )

        "id" -> ReviewUiText(
            title = "Ulasan",
            intro = "Ulangi bagian yang sulit berdasarkan jenis dan langsung kembali ke latihannya.",
            empty = "Tidak ada yang perlu diulang.",
            practiceAgain = "Latih lagi",
            resolved = "Sudah paham",
            categories = categories(
                "Membaca", "Kosakata", "Kalimat", "Mendengarkan", "Berbicara",
                "Ekspresi saya", "Percakapan", "Lainnya",
            ),
        )

        else -> ReviewUiText(
            title = "Review",
            intro = "See what was difficult by skill, then jump straight back into that practice.",
            empty = "Nothing needs review.",
            practiceAgain = "Practice again",
            resolved = "I know this now",
            categories = categories(
                "Reading", "Words", "Sentences", "Listening", "Speaking",
                "My expressions", "Conversation", "Other",
            ),
        )
    }
}
