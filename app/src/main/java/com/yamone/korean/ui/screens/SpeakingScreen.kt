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
    focusSentenceId: String? = null,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onLessonNeedsReview: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val ui = speakingUiText(languageCode)
    val orderedLessons = remember(focusSentenceId) {
        if (focusSentenceId == null) {
            BasicSentenceCatalog.lessons
        } else {
            BasicSentenceCatalog.lessons.sortedBy { lesson ->
                if (lesson.id == focusSentenceId) 0 else 1
            }
        }
    }
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
            onLessonNeedsReview(speakingId)
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
        } else {
            onLessonNeedsReview(speakingId)
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
            items = orderedLessons,
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
        intro = "Lee una frase \u00fatil, dilo en voz alta y compara lo que reconoci\u00f3 el dispositivo. La comprobaci\u00f3n usa reconocimiento de voz coreano sin conexi\u00f3n cuando est\u00e1 disponible.",
        targetLabel = "Di esta frase",
        speakLabel = "Hablar",
        recognizedLabel = "Reconocido",
        matchedLabel = "\u00a1Muy bien! La frase coincide.",
        retryLabel = "Casi. Int\u00e9ntalo otra vez.",
        completedLabel = "Pr\u00e1ctica oral completada",
        unavailableLabel = "El reconocimiento de voz coreano sin conexi\u00f3n no est\u00e1 disponible en este dispositivo. Puedes seguir estudiando, pero la comprobaci\u00f3n por voz queda desactivada.",
        canceledLabel = "La escucha se cancel\u00f3. Int\u00e9ntalo otra vez cuando quieras.",
        continueLabel = "Continuar a conversaci\u00f3n",
    )

    "fr" -> SpeakingUiText(
        title = "Parler cor\u00e9en",
        intro = "Lis une phrase utile, dis-la \u00e0 voix haute et compare ce que l'appareil a reconnu. La v\u00e9rification utilise la reconnaissance vocale cor\u00e9enne hors ligne lorsqu'elle est disponible.",
        targetLabel = "Dis cette phrase",
        speakLabel = "Parler",
        recognizedLabel = "Reconnu",
        matchedLabel = "Tr\u00e8s bien ! La phrase correspond.",
        retryLabel = "Presque. R\u00e9essaie.",
        completedLabel = "Expression orale termin\u00e9e",
        unavailableLabel = "La reconnaissance vocale cor\u00e9enne hors ligne n'est pas disponible sur cet appareil. Tu peux continuer \u00e0 apprendre, mais la v\u00e9rification vocale est d\u00e9sactiv\u00e9e.",
        canceledLabel = "L'\u00e9coute a \u00e9t\u00e9 annul\u00e9e. R\u00e9essaie quand tu veux.",
        continueLabel = "Continuer vers la conversation",
    )

    "vi" -> SpeakingUiText(
        title = "N\u00f3i ti\u1ebfng H\u00e0n",
        intro = "Xem m\u1ed9t c\u00e2u h\u1eefu \u00edch, n\u00f3i th\u00e0nh ti\u1ebfng r\u1ed3i so s\u00e1nh v\u1edbi k\u1ebft qu\u1ea3 thi\u1ebft b\u1ecb nh\u1eadn d\u1ea1ng. \u1ee8ng d\u1ee5ng \u01b0u ti\u00ean nh\u1eadn d\u1ea1ng gi\u1ecdng n\u00f3i ti\u1ebfng H\u00e0n ngo\u1ea1i tuy\u1ebfn tr\u00ean thi\u1ebft b\u1ecb.",
        targetLabel = "H\u00e3y n\u00f3i c\u00e2u n\u00e0y",
        speakLabel = "N\u00f3i",
        recognizedLabel = "\u0110\u00e3 nh\u1eadn d\u1ea1ng",
        matchedLabel = "R\u1ea5t t\u1ed1t! C\u00e2u n\u00f3i kh\u1edbp.",
        retryLabel = "G\u1ea7n \u0111\u00fang r\u1ed3i. H\u00e3y th\u1eed l\u1ea1i.",
        completedLabel = "\u0110\u00e3 ho\u00e0n th\u00e0nh luy\u1ec7n n\u00f3i",
        unavailableLabel = "Thi\u1ebft b\u1ecb n\u00e0y kh\u00f4ng c\u00f3 nh\u1eadn d\u1ea1ng gi\u1ecdng n\u00f3i ti\u1ebfng H\u00e0n ngo\u1ea1i tuy\u1ebfn. B\u1ea1n v\u1eabn c\u00f3 th\u1ec3 h\u1ecdc ti\u1ebfp nh\u01b0ng ch\u1ee9c n\u0103ng ki\u1ec3m tra gi\u1ecdng n\u00f3i s\u1ebd b\u1ecb t\u1eaft.",
        canceledLabel = "\u0110\u00e3 h\u1ee7y nghe. B\u1ea1n c\u00f3 th\u1ec3 th\u1eed l\u1ea1i b\u1ea5t c\u1ee9 l\u00fac n\u00e0o.",
        continueLabel = "Ti\u1ebfp t\u1ee5c sang h\u1ed9i tho\u1ea1i",
    )

    "th" -> SpeakingUiText(
        title = "\u0e1e\u0e39\u0e14\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35",
        intro = "\u0e14\u0e39\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e17\u0e35\u0e48\u0e43\u0e0a\u0e49\u0e08\u0e23\u0e34\u0e07 \u0e1e\u0e39\u0e14\u0e2d\u0e2d\u0e01\u0e40\u0e2a\u0e35\u0e22\u0e07 \u0e41\u0e25\u0e49\u0e27\u0e40\u0e1b\u0e23\u0e35\u0e22\u0e1a\u0e40\u0e17\u0e35\u0e22\u0e1a\u0e01\u0e31\u0e1a\u0e02\u0e49\u0e2d\u0e04\u0e27\u0e32\u0e21\u0e17\u0e35\u0e48\u0e2d\u0e38\u0e1b\u0e01\u0e23\u0e13\u0e4c\u0e1f\u0e31\u0e07\u0e44\u0e14\u0e49 \u0e42\u0e14\u0e22\u0e43\u0e2b\u0e49\u0e04\u0e27\u0e32\u0e21\u0e2a\u0e33\u0e04\u0e31\u0e0d\u0e01\u0e31\u0e1a\u0e01\u0e32\u0e23\u0e23\u0e39\u0e49\u0e08\u0e33\u0e40\u0e2a\u0e35\u0e22\u0e07\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e41\u0e1a\u0e1a\u0e2d\u0e2d\u0e1f\u0e44\u0e25\u0e19\u0e4c\u0e1a\u0e19\u0e2d\u0e38\u0e1b\u0e01\u0e23\u0e13\u0e4c",
        targetLabel = "\u0e1e\u0e39\u0e14\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e19\u0e35\u0e49",
        speakLabel = "\u0e1e\u0e39\u0e14",
        recognizedLabel = "\u0e1f\u0e31\u0e07\u0e44\u0e14\u0e49\u0e27\u0e48\u0e32",
        matchedLabel = "\u0e14\u0e35\u0e21\u0e32\u0e01! \u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e15\u0e23\u0e07\u0e01\u0e31\u0e19",
        retryLabel = "\u0e40\u0e01\u0e37\u0e2d\u0e1a\u0e41\u0e25\u0e49\u0e27 \u0e25\u0e2d\u0e07\u0e2d\u0e35\u0e01\u0e04\u0e23\u0e31\u0e49\u0e07",
        completedLabel = "\u0e1d\u0e36\u0e01\u0e1e\u0e39\u0e14\u0e40\u0e2a\u0e23\u0e47\u0e08\u0e41\u0e25\u0e49\u0e27",
        unavailableLabel = "\u0e2d\u0e38\u0e1b\u0e01\u0e23\u0e13\u0e4c\u0e19\u0e35\u0e49\u0e44\u0e21\u0e48\u0e21\u0e35\u0e01\u0e32\u0e23\u0e23\u0e39\u0e49\u0e08\u0e33\u0e40\u0e2a\u0e35\u0e22\u0e07\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e41\u0e1a\u0e1a\u0e2d\u0e2d\u0e1f\u0e44\u0e25\u0e19\u0e4c \u0e04\u0e38\u0e13\u0e22\u0e31\u0e07\u0e40\u0e23\u0e35\u0e22\u0e19\u0e15\u0e48\u0e2d\u0e44\u0e14\u0e49 \u0e41\u0e15\u0e48\u0e01\u0e32\u0e23\u0e15\u0e23\u0e27\u0e08\u0e40\u0e2a\u0e35\u0e22\u0e07\u0e08\u0e30\u0e16\u0e39\u0e01\u0e1b\u0e34\u0e14",
        canceledLabel = "\u0e22\u0e01\u0e40\u0e25\u0e34\u0e01\u0e01\u0e32\u0e23\u0e1f\u0e31\u0e07\u0e41\u0e25\u0e49\u0e27 \u0e25\u0e2d\u0e07\u0e43\u0e2b\u0e21\u0e48\u0e44\u0e14\u0e49\u0e17\u0e38\u0e01\u0e40\u0e21\u0e37\u0e48\u0e2d",
        continueLabel = "\u0e44\u0e1b\u0e1d\u0e36\u0e01\u0e2a\u0e19\u0e17\u0e19\u0e32",
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
