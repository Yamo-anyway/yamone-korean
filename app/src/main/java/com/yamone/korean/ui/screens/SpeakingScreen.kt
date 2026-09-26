package com.yamone.korean.ui.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicSentenceCatalog
import com.yamone.korean.learning.BasicSentenceLesson
import java.util.Locale
import kotlin.math.max

private data class SpeakingUiText(
    val title: String,
    val intro: String,
    val targetLabel: String,
    val speakLabel: String,
    val recognizedLabel: String,
    val matchedLabel: String,
    val retryLabel: String,
    val completedLabel: String,
    val unavailableLabel: String,
    val canceledLabel: String,
    val continueLabel: String,
)

private data class SpeakingAttempt(
    val recognizedText: String,
    val similarity: Float,
)

@Composable
fun SpeakingScreen(
    languageCode: String?,
    completedLessonIds: Set<String>,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val ui = speakingUiText(languageCode)
    val onDeviceRecognitionAvailable = remember(context) {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
    }

    var activeLesson by remember { mutableStateOf<BasicSentenceLesson?>(null) }
    var attempts by remember { mutableStateOf<Map<String, SpeakingAttempt>>(emptyMap()) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        val lesson = activeLesson
        if (lesson == null) return@rememberLauncherForActivityResult

        val speakingId = "speaking_${lesson.id}"
        if (result.resultCode != Activity.RESULT_OK) {
            statusMessage = ui.canceledLabel
            activeLesson = null
            return@rememberLauncherForActivityResult
        }

        val candidates = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            .orEmpty()

        val best = candidates
            .map { candidate -> candidate to koreanSimilarity(candidate, lesson.korean) }
            .maxByOrNull { it.second }

        attempts = attempts + (
            speakingId to SpeakingAttempt(
                recognizedText = best?.first.orEmpty(),
                similarity = best?.second ?: 0f,
            )
        )

        if ((best?.second ?: 0f) >= PASSING_SIMILARITY) {
            onLessonCompleted(speakingId)
        }

        statusMessage = null
        activeLesson = null
    }

    fun startSpeaking(lesson: BasicSentenceLesson) {
        val speakingId = "speaking_${lesson.id}"
        onLessonOpened(speakingId)

        if (!onDeviceRecognitionAvailable) {
            statusMessage = ui.unavailableLabel
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            putExtra(RecognizerIntent.EXTRA_PROMPT, lesson.korean)
        }

        activeLesson = lesson
        statusMessage = null

        try {
            speechLauncher.launch(intent)
        } catch (_: ActivityNotFoundException) {
            activeLesson = null
            statusMessage = ui.unavailableLabel
        }
    }

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

            if (!onDeviceRecognitionAvailable) {
                Text(
                    text = ui.unavailableLabel,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            statusMessage?.let { message ->
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        items(
            items = BasicSentenceCatalog.lessons,
            key = { it.id },
        ) { lesson ->
            val speakingId = "speaking_${lesson.id}"
            SpeakingLessonCard(
                lesson = lesson,
                languageCode = languageCode,
                completed = speakingId in completedLessonIds,
                attempt = attempts[speakingId],
                ui = ui,
                enabled = onDeviceRecognitionAvailable && activeLesson == null,
                onSpeak = { startSpeaking(lesson) },
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
private fun SpeakingLessonCard(
    lesson: BasicSentenceLesson,
    languageCode: String?,
    completed: Boolean,
    attempt: SpeakingAttempt?,
    ui: SpeakingUiText,
    enabled: Boolean,
    onSpeak: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = ui.targetLabel,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = lesson.korean,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = lesson.meaning(languageCode),
                style = MaterialTheme.typography.bodyMedium,
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                onClick = onSpeak,
            ) {
                Text(ui.speakLabel)
            }

            attempt?.let { result ->
                Text(
                    text = "${ui.recognizedLabel}: ${result.recognizedText}",
                    style = MaterialTheme.typography.bodyLarge,
                )

                val percent = (result.similarity * 100).toInt()
                Text(
                    text = if (result.similarity >= PASSING_SIMILARITY) {
                        "${ui.matchedLabel} ($percent%)"
                    } else {
                        "${ui.retryLabel} ($percent%)"
                    },
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            if (attempt == null && completed) {
                Text(
                    text = ui.completedLabel,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

private const val PASSING_SIMILARITY = 0.78f

private fun koreanSimilarity(spoken: String, target: String): Float {
    val left = normalizeKoreanSpeech(spoken)
    val right = normalizeKoreanSpeech(target)
    if (left.isEmpty() || right.isEmpty()) return 0f
    if (left == right) return 1f

    val distance = levenshteinDistance(left, right)
    return 1f - (distance.toFloat() / max(left.length, right.length).toFloat())
}

private fun normalizeKoreanSpeech(value: String): String =
    value.lowercase(Locale.KOREAN).filter { it.isLetterOrDigit() }

private fun levenshteinDistance(left: String, right: String): Int {
    var previous = IntArray(right.length + 1) { it }

    left.forEachIndexed { leftIndex, leftChar ->
        val current = IntArray(right.length + 1)
        current[0] = leftIndex + 1

        right.forEachIndexed { rightIndex, rightChar ->
            val insertion = current[rightIndex] + 1
            val deletion = previous[rightIndex + 1] + 1
            val substitution = previous[rightIndex] + if (leftChar == rightChar) 0 else 1
            current[rightIndex + 1] = minOf(insertion, deletion, substitution)
        }

        previous = current
    }

    return previous[right.length]
}

private fun speakingUiText(languageCode: String?): SpeakingUiText = when (languageCode) {
    "es" -> SpeakingUiText(
        title = "Habla coreano",
        intro = "Lee una frase útil, dilo en voz alta y compara lo que reconoció el dispositivo. La comprobación usa reconocimiento de voz coreano sin conexión cuando está disponible.",
        targetLabel = "Di esta frase",
        speakLabel = "Hablar",
        recognizedLabel = "Reconocido",
        matchedLabel = "¡Muy bien! La frase coincide.",
        retryLabel = "Casi. Inténtalo otra vez.",
        completedLabel = "Práctica oral completada",
        unavailableLabel = "El reconocimiento de voz coreano sin conexión no está disponible en este dispositivo. Puedes seguir estudiando, pero la comprobación por voz queda desactivada.",
        canceledLabel = "La escucha se canceló. Inténtalo otra vez cuando quieras.",
        continueLabel = "Continuar a conversación",
    )

    "fr" -> SpeakingUiText(
        title = "Parler coréen",
        intro = "Lis une phrase utile, dis-la à voix haute et compare ce que l'appareil a reconnu. La vérification utilise la reconnaissance vocale coréenne hors ligne lorsqu'elle est disponible.",
        targetLabel = "Dis cette phrase",
        speakLabel = "Parler",
        recognizedLabel = "Reconnu",
        matchedLabel = "Très bien ! La phrase correspond.",
        retryLabel = "Presque. Réessaie.",
        completedLabel = "Expression orale terminée",
        unavailableLabel = "La reconnaissance vocale coréenne hors ligne n'est pas disponible sur cet appareil. Tu peux continuer à apprendre, mais la vérification vocale est désactivée.",
        canceledLabel = "L'écoute a été annulée. Réessaie quand tu veux.",
        continueLabel = "Continuer vers la conversation",
    )

    "vi" -> SpeakingUiText(
        title = "Nói tiếng Hàn",
        intro = "Xem một câu hữu ích, nói thành tiếng rồi so sánh với kết quả thiết bị nhận dạng. Ứng dụng ưu tiên nhận dạng giọng nói tiếng Hàn ngoại tuyến trên thiết bị.",
        targetLabel = "Hãy nói câu này",
        speakLabel = "Nói",
        recognizedLabel = "Đã nhận dạng",
        matchedLabel = "Rất tốt! Câu nói khớp.",
        retryLabel = "Gần đúng rồi. Hãy thử lại.",
        completedLabel = "Đã hoàn thành luyện nói",
        unavailableLabel = "Thiết bị này không có nhận dạng giọng nói tiếng Hàn ngoại tuyến. Bạn vẫn có thể học tiếp nhưng chức năng kiểm tra giọng nói sẽ bị tắt.",
        canceledLabel = "Đã hủy nghe. Bạn có thể thử lại bất cứ lúc nào.",
        continueLabel = "Tiếp tục sang hội thoại",
    )

    "th" -> SpeakingUiText(
        title = "พูดภาษาเกาหลี",
        intro = "ดูประโยคที่ใช้จริง พูดออกเสียง แล้วเปรียบเทียบกับข้อความที่อุปกรณ์ฟังได้ โดยให้ความสำคัญกับการรู้จำเสียงภาษาเกาหลีแบบออฟไลน์บนอุปกรณ์",
        targetLabel = "พูดประโยคนี้",
        speakLabel = "พูด",
        recognizedLabel = "ฟังได้ว่า",
        matchedLabel = "ดีมาก! ประโยคตรงกัน",
        retryLabel = "เกือบแล้ว ลองอีกครั้ง",
        completedLabel = "ฝึกพูดเสร็จแล้ว",
        unavailableLabel = "อุปกรณ์นี้ไม่มีการรู้จำเสียงภาษาเกาหลีแบบออฟไลน์ คุณยังเรียนต่อได้ แต่การตรวจเสียงจะถูกปิด",
        canceledLabel = "ยกเลิกการฟังแล้ว ลองใหม่ได้ทุกเมื่อ",
        continueLabel = "ไปฝึกสนทนา",
    )

    "id" -> SpeakingUiText(
        title = "Berbicara bahasa Korea",
        intro = "Lihat kalimat yang berguna, ucapkan dengan suara keras, lalu bandingkan hasil yang dikenali perangkat. Aplikasi memprioritaskan pengenal suara Korea offline di perangkat.",
        targetLabel = "Ucapkan kalimat ini",
        speakLabel = "Bicara",
        recognizedLabel = "Dikenali",
        matchedLabel = "Bagus! Kalimatnya cocok.",
        retryLabel = "Hampir. Coba lagi.",
        completedLabel = "Latihan berbicara selesai",
        unavailableLabel = "Pengenalan suara Korea offline tidak tersedia di perangkat ini. Anda tetap dapat belajar, tetapi pemeriksaan suara dinonaktifkan.",
        canceledLabel = "Pengenalan suara dibatalkan. Coba lagi kapan saja.",
        continueLabel = "Lanjut ke percakapan",
    )

    else -> SpeakingUiText(
        title = "Speak Korean",
        intro = "Read a useful sentence, say it aloud, and compare what your device recognized. The app prefers Korean on-device speech recognition so practice can stay offline.",
        targetLabel = "Say this sentence",
        speakLabel = "Speak",
        recognizedLabel = "Recognized",
        matchedLabel = "Great! Your sentence matched.",
        retryLabel = "Almost. Try it again.",
        completedLabel = "Speaking completed",
        unavailableLabel = "Offline Korean speech recognition is not available on this device. You can keep learning, but voice checking is disabled.",
        canceledLabel = "Listening was canceled. Try again whenever you are ready.",
        continueLabel = "Continue to conversation",
    )
}
