package com.yamone.korean.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.yamone.korean.navigation.learningDestinations

private enum class NextStudyMode {
    REVIEW,
    CONTINUE,
    NEXT,
    COMPLETE,
}

private data class NextStudyRecommendation(
    val destination: AppDestination?,
    val remaining: Int,
    val mode: NextStudyMode,
)

@Composable
internal fun HomeProgressSummary(
    languageCode: String?,
    learningProgress: LearningProgressState,
    onOpenStage: (AppDestination) -> Unit,
    onOpenReview: () -> Unit,
) {
    val currentTitle = learningDestinations
        .firstOrNull { it.route == learningProgress.currentStageRoute }
        ?.title
        ?: learningDestinations.first().title
    val reviewCount = learningProgress.reviewLessonIds.size
    val completedCount = learningProgress.completedLessonIds.size

    val labels = when (languageCode) {
        "es" -> listOf("Mi progreso", "Etapa actual", "Practicado", "Para repasar")
        "fr" -> listOf("Ma progression", "Étape actuelle", "Pratiqué", "À réviser")
        "vi" -> listOf("Tiến độ của tôi", "Bước hiện tại", "Đã luyện", "Cần ôn")
        "th" -> listOf("ความคืบหน้าของฉัน", "ขั้นตอนปัจจุบัน", "ฝึกแล้ว", "ต้องทบทวน")
        "id" -> listOf("Progres saya", "Tahap saat ini", "Dilatih", "Perlu diulang")
        else -> listOf("My progress", "Current stage", "Practiced", "Needs review")
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
        }
    }

    HomeNextStudyCard(
        languageCode = languageCode,
        learningProgress = learningProgress,
        onOpenStage = onOpenStage,
        onOpenReview = onOpenReview,
    )

    StageProgressBreakdown(
        languageCode = languageCode,
        learningProgress = learningProgress,
        onOpenStage = onOpenStage,
    )
}

@Composable
private fun HomeNextStudyCard(
    languageCode: String?,
    learningProgress: LearningProgressState,
    onOpenStage: (AppDestination) -> Unit,
    onOpenReview: () -> Unit,
) {
    val recommendation = recommendNextStudy(learningProgress)
    val text = nextStudyText(languageCode)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (recommendation.mode == NextStudyMode.COMPLETE) {
                Text(text[7], style = MaterialTheme.typography.titleMedium)
                Text(text[8], style = MaterialTheme.typography.bodyMedium)
                return@Column
            }

            val destination = recommendation.destination ?: return@Column
            Text(text[0], style = MaterialTheme.typography.labelLarge)
            Text(
                localizedProgressStageTitle(destination, languageCode),
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                when (recommendation.mode) {
                    NextStudyMode.REVIEW -> text[1]
                    NextStudyMode.CONTINUE -> text[2]
                    NextStudyMode.NEXT -> text[3]
                    NextStudyMode.COMPLETE -> text[8]
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                if (recommendation.mode == NextStudyMode.REVIEW) {
                    "${text[5]}: ${recommendation.remaining}"
                } else {
                    "${text[4]}: ${recommendation.remaining}"
                },
                style = MaterialTheme.typography.bodySmall,
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (destination == AppDestination.Review) onOpenReview()
                    else onOpenStage(destination)
                },
            ) {
                Text(text[6])
            }
        }
    }
}

private fun recommendNextStudy(
    learningProgress: LearningProgressState,
): NextStudyRecommendation {
    if (learningProgress.reviewLessonIds.isNotEmpty()) {
        return NextStudyRecommendation(
            destination = AppDestination.Review,
            remaining = learningProgress.reviewLessonIds.size,
            mode = NextStudyMode.REVIEW,
        )
    }

    data class StageState(
        val destination: AppDestination,
        val remaining: Int,
    )

    val completed = learningProgress.completedLessonIds
    val stages = listOf(
        StageState(
            AppDestination.Syllable,
            BasicSyllableCatalog.lessons.count {
                it.id !in learningProgress.masteredSyllableLessonIds
            },
        ),
        StageState(
            AppDestination.Word,
            BasicWordCatalog.lessons.count { it.id !in completed },
        ),
        StageState(
            AppDestination.Sentence,
            BasicSentenceCatalog.lessons.count { it.id !in completed },
        ),
        StageState(
            AppDestination.Listening,
            BasicSentenceCatalog.lessons.count { "listening_" + it.id !in completed },
        ),
        StageState(
            AppDestination.Speaking,
            BasicSentenceCatalog.lessons.count { "speaking_" + it.id !in completed },
        ),
        StageState(
            AppDestination.Expression,
            SelfExpressionCatalog.lessons.count { "expression_" + it.id !in completed },
        ),
        StageState(
            AppDestination.Conversation,
            ConversationCatalog.lessons.count { "conversation_" + it.id !in completed },
        ),
    )

    stages.firstOrNull {
        it.destination.route == learningProgress.currentStageRoute && it.remaining > 0
    }?.let {
        return NextStudyRecommendation(it.destination, it.remaining, NextStudyMode.CONTINUE)
    }

    stages.firstOrNull { it.remaining > 0 }?.let {
        return NextStudyRecommendation(it.destination, it.remaining, NextStudyMode.NEXT)
    }

    return NextStudyRecommendation(null, 0, NextStudyMode.COMPLETE)
}

private fun nextStudyText(languageCode: String?): List<String> = when (languageCode) {
    "es" -> listOf(
        "Siguiente estudio",
        "Repasa primero lo que te costó antes de avanzar.",
        "Continúa desde la etapa en la que estabas.",
        "Esta es la siguiente etapa que aún no has terminado.",
        "Quedan",
        "Elementos para repasar",
        "Empezar",
        "Ruta principal completada",
        "Terminaste las etapas principales. Sigue practicando conversación o repasa cuando quieras.",
    )
    "fr" -> listOf(
        "Prochaine étude",
        "Révise d'abord les points difficiles avant d'avancer.",
        "Continue à l'étape où tu t'étais arrêté.",
        "C'est la prochaine étape encore incomplète.",
        "Restant",
        "Éléments à réviser",
        "Commencer",
        "Parcours principal terminé",
        "Tu as terminé les étapes principales. Continue la conversation ou révise quand tu veux.",
    )
    "vi" -> listOf(
        "Bài học tiếp theo",
        "Ôn lại phần còn yếu trước khi học tiếp.",
        "Tiếp tục từ bước bạn đang học dở.",
        "Đây là bước tiếp theo bạn chưa hoàn thành.",
        "Còn lại",
        "Mục cần ôn",
        "Bắt đầu",
        "Đã hoàn thành lộ trình chính",
        "Bạn đã hoàn thành các bước chính. Hãy tiếp tục hội thoại hoặc ôn lại khi cần.",
    )
    "th" -> listOf(
        "บทเรียนถัดไป",
        "ทบทวนจุดที่ยังไม่แม่นก่อนเรียนต่อ",
        "เรียนต่อจากขั้นที่ค้างไว้",
        "นี่คือขั้นถัดไปที่ยังเรียนไม่ครบ",
        "เหลือ",
        "รายการที่ต้องทบทวน",
        "เริ่ม",
        "เรียนเส้นทางหลักครบแล้ว",
        "เรียนขั้นหลักครบแล้ว ฝึกสนทนาต่อหรือกลับมาทบทวนได้ทุกเมื่อ",
    )
    "id" -> listOf(
        "Belajar berikutnya",
        "Ulangi bagian yang masih sulit sebelum melanjutkan.",
        "Lanjutkan dari tahap terakhir Anda.",
        "Ini tahap berikutnya yang belum selesai.",
        "Tersisa",
        "Perlu diulang",
        "Mulai",
        "Jalur utama selesai",
        "Tahap utama sudah selesai. Lanjutkan latihan percakapan atau ulas kapan saja.",
    )
    else -> listOf(
        "Next study",
        "Review weak points before moving on.",
        "Continue from the stage you were working on.",
        "This is the next unfinished stage.",
        "Remaining",
        "Review items",
        "Start",
        "Core path complete",
        "You finished the main stages. Keep practicing conversation or review whenever you want.",
    )
}
