package com.yamone.korean.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.data.LearningProgressState
import com.yamone.korean.navigation.learningDestinations

@Composable
internal fun HomeProgressSummary(
    languageCode: String?,
    learningProgress: LearningProgressState,
    onOpenStage: (com.yamone.korean.navigation.AppDestination) -> Unit,
    onOpenReview: () -> Unit,
) {
    val currentTitle = learningDestinations
        .firstOrNull { it.route == learningProgress.currentStageRoute }
        ?.title
        ?: learningDestinations.first().title
    val reviewCount = learningProgress.reviewLessonIds.size
    val completedCount = learningProgress.completedLessonIds.size

    val labels = when (languageCode) {
        "es" -> listOf("Mi progreso", "Etapa actual", "Practicado", "Para repasar", "Repasar ahora")
        "fr" -> listOf("Ma progression", "Étape actuelle", "Pratiqué", "À réviser", "Réviser maintenant")
        "vi" -> listOf("Tiến độ của tôi", "Bước hiện tại", "Đã luyện", "Cần ôn", "Ôn ngay")
        "th" -> listOf("ความคืบหน้าของฉัน", "ขั้นตอนปัจจุบัน", "ฝึกแล้ว", "ต้องทบทวน", "ทบทวนตอนนี้")
        "id" -> listOf("Progres saya", "Tahap saat ini", "Dilatih", "Perlu diulang", "Ulangi sekarang")
        else -> listOf("My progress", "Current stage", "Practiced", "Needs review", "Review now")
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(labels[0], style = MaterialTheme.typography.titleMedium)
            Text("${labels[1]}: $currentTitle", style = MaterialTheme.typography.bodyLarge)
            Text("${labels[2]}: $completedCount", style = MaterialTheme.typography.bodyMedium)
            Text("${labels[3]}: $reviewCount", style = MaterialTheme.typography.bodyMedium)
            if (reviewCount > 0) {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onOpenReview,
                ) {
                    Text("${labels[4]} ($reviewCount)")
                }
            }
        }
    }

    StageProgressBreakdown(
        languageCode = languageCode,
        learningProgress = learningProgress,
        onOpenStage = onOpenStage,
    )
}
