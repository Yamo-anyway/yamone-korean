package com.yamone.korean.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.data.LearningProgressState
import com.yamone.korean.learning.BasicSentenceCatalog
import com.yamone.korean.learning.BasicSyllableCatalog
import com.yamone.korean.learning.BasicWordCatalog
import com.yamone.korean.learning.ConversationCatalog
import com.yamone.korean.learning.SelfExpressionCatalog
import com.yamone.korean.navigation.AppDestination

private data class StageProgressItem(
    val destination: AppDestination,
    val completed: Int,
    val total: Int,
    val reviewCount: Int,
)

private data class StageProgressLabels(
    val title: String,
    val completed: String,
    val needsReview: String,
)

@Composable
internal fun StageProgressBreakdown(
    languageCode: String?,
    learningProgress: LearningProgressState,
    onOpenStage: (AppDestination) -> Unit,
) {
    val labels = stageProgressLabels(languageCode)
    val stages = stageProgressItems(learningProgress)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = labels.title,
                style = MaterialTheme.typography.titleMedium,
            )

            stages.forEachIndexed { index, item ->
                val fraction = if (item.total == 0) 0f
                else (item.completed.toFloat() / item.total.toFloat()).coerceIn(0f, 1f)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenStage(item.destination) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = localizedProgressStageTitle(item.destination, languageCode),
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(
                            text = if (item.total > 0 && item.completed >= item.total) {
                                labels.completed
                            } else {
                                item.completed.toString() + "/" + item.total
                            },
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    LinearProgressIndicator(
                        progress = { fraction },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (item.reviewCount > 0) {
                        Text(
                            text = labels.needsReview + ": " + item.reviewCount,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }

                if (index != stages.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

private fun stageProgressItems(
    learningProgress: LearningProgressState,
): List<StageProgressItem> {
    val syllableIds = BasicSyllableCatalog.lessons.map { it.id }.toSet()
    val wordIds = BasicWordCatalog.lessons.map { it.id }.toSet()
    val sentenceIds = BasicSentenceCatalog.lessons.map { it.id }.toSet()
    val listeningIds = BasicSentenceCatalog.lessons.map { "listening_" + it.id }.toSet()
    val speakingIds = BasicSentenceCatalog.lessons.map { "speaking_" + it.id }.toSet()
    val expressionIds = SelfExpressionCatalog.lessons.map { "expression_" + it.id }.toSet()
    val conversationIds = ConversationCatalog.lessons.map { "conversation_" + it.id }.toSet()

    fun standard(
        destination: AppDestination,
        ids: Set<String>,
    ) = StageProgressItem(
        destination = destination,
        completed = learningProgress.completedLessonIds.count { it in ids },
        total = ids.size,
        reviewCount = learningProgress.reviewLessonIds.count { it in ids },
    )

    return listOf(
        StageProgressItem(
            destination = AppDestination.Syllable,
            completed = learningProgress.masteredSyllableLessonIds.count { it in syllableIds },
            total = syllableIds.size,
            reviewCount = learningProgress.reviewLessonIds.count { it in syllableIds },
        ),
        standard(AppDestination.Word, wordIds),
        standard(AppDestination.Sentence, sentenceIds),
        standard(AppDestination.Listening, listeningIds),
        standard(AppDestination.Speaking, speakingIds),
        standard(AppDestination.Expression, expressionIds),
        standard(AppDestination.Conversation, conversationIds),
    )
}

private fun stageProgressLabels(languageCode: String?): StageProgressLabels = when (languageCode) {
    "es" -> StageProgressLabels("Progreso por etapa", "Completado", "Para repasar")
    "fr" -> StageProgressLabels("Progression par étape", "Terminé", "À réviser")
    "vi" -> StageProgressLabels("Tiến độ theo bước", "Hoàn thành", "Cần ôn")
    "th" -> StageProgressLabels("ความคืบหน้าแต่ละขั้น", "เสร็จแล้ว", "ต้องทบทวน")
    "id" -> StageProgressLabels("Progres per tahap", "Selesai", "Perlu diulang")
    else -> StageProgressLabels("Progress by stage", "Completed", "Needs review")
}

private fun localizedProgressStageTitle(
    destination: AppDestination,
    languageCode: String?,
): String = when (languageCode) {
    "es" -> when (destination) {
        AppDestination.Syllable -> "Sílabas y consonantes finales"
        AppDestination.Word -> "Palabras"
        AppDestination.Sentence -> "Frases"
        AppDestination.Listening -> "Escucha"
        AppDestination.Speaking -> "Habla"
        AppDestination.Expression -> "Mis expresiones"
        AppDestination.Conversation -> "Conversación"
        else -> destination.title
    }
    "fr" -> when (destination) {
        AppDestination.Syllable -> "Syllabes et consonnes finales"
        AppDestination.Word -> "Mots"
        AppDestination.Sentence -> "Phrases"
        AppDestination.Listening -> "Écoute"
        AppDestination.Speaking -> "Expression orale"
        AppDestination.Expression -> "Mes expressions"
        AppDestination.Conversation -> "Conversation"
        else -> destination.title
    }
    "vi" -> when (destination) {
        AppDestination.Syllable -> "Âm tiết và phụ âm cuối"
        AppDestination.Word -> "Từ"
        AppDestination.Sentence -> "Câu"
        AppDestination.Listening -> "Nghe"
        AppDestination.Speaking -> "Nói"
        AppDestination.Expression -> "Câu của tôi"
        AppDestination.Conversation -> "Hội thoại"
        else -> destination.title
    }
    "th" -> when (destination) {
        AppDestination.Syllable -> "พยางค์และตัวสะกด"
        AppDestination.Word -> "คำศัพท์"
        AppDestination.Sentence -> "ประโยค"
        AppDestination.Listening -> "การฟัง"
        AppDestination.Speaking -> "การพูด"
        AppDestination.Expression -> "ประโยคของฉัน"
        AppDestination.Conversation -> "บทสนทนา"
        else -> destination.title
    }
    "id" -> when (destination) {
        AppDestination.Syllable -> "Suku kata dan konsonan akhir"
        AppDestination.Word -> "Kata"
        AppDestination.Sentence -> "Kalimat"
        AppDestination.Listening -> "Mendengarkan"
        AppDestination.Speaking -> "Berbicara"
        AppDestination.Expression -> "Ekspresi saya"
        AppDestination.Conversation -> "Percakapan"
        else -> destination.title
    }
    else -> destination.title
}
