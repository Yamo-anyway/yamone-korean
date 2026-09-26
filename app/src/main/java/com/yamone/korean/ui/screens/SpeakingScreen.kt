package com.yamone.korean.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
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
import androidx.compose.runtime.DisposableEffect
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
    val listeningLabel: String,
    val recognizedLabel: String,
    val matchedLabel: String,
    val retryLabel: String,
    val completedLabel: String,
    val unavailableLabel: String,
    val permissionDeniedLabel: String,
    val errorLabel: String,
    val continueLabel: String,
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

    var recognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    var activeLessonId by remember { mutableStateOf<String?>(null) }
    var isListening by remember { mutableStateOf(false) }
    var recognitionError by remember { mutableStateOf<String?>(null) }
    var resultConsumer by remember { mutableStateOf<(List<String>) -> Unit>({}) }
    var pendingStart by remember { mutableStateOf<(() -> Unit)?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            pendingStart?.invoke()
        } else {
            isListening = false
            activeLessonId = null
            recognitionError = ui.permissionDeniedLabel
        }
        pendingStart = null
    }

    DisposableEffect(context, onDeviceRecognitionAvailable, languageCode) {
        if (!onDeviceRecognitionAvailable) {
            onDispose { }
        } else {
            val speechRecognizer =
                SpeechRecognizer.createOnDeviceSpeechRecognizer(context.applicationContext)

            speechRecognizer.setRecognitionListener(
                object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) = Unit
                    override fun onBeginningOfSpeech() = Unit
                    override fun onRmsChanged(rmsdB: Float) = Unit
                    override fun onBufferReceived(buffer: ByteArray?) = Unit
                    override fun onEndOfSpeech() = Unit

                    override fun onError(error: Int) {
                        isListening = false
                        recognitionError = ui.errorLabel
                    }

                    override fun onResults(results: Bundle?) {
                        isListening = false
                        recognitionError = null
                        val candidates = results
                            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            .orEmpty()
                        resultConsumer(candidates)
                    }

                    override fun onPartialResults(partialResults: Bundle?) = Unit
                    override fun onEvent(eventType: Int, params: Bundle?) = Unit
                },
            )
            recognizer = speechRecognizer

            onDispose {
                speechRecognizer.cancel()
                speechRecognizer.destroy()
                recognizer = null
            }
        }
    }

    fun startSpeakingLesson(
        speakingId: String,
        consumeResults: (List<String>) -> Unit,
    ) {
        onLessonOpened(speakingId)
        recognitionError = null

        if (!onDeviceRecognitionAvailable) {
            recognitionError = ui.unavailableLabel
            return
        }

        val startRecognition = {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREA.toLanguageTag())
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
                putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            }

            activeLessonId = speakingId
            resultConsumer = consumeResults
            isListening = true
            recognizer?.startListening(intent)
        }

        if (
            context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            startRecognition()
        } else {
            pendingStart = startRecognition
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
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

            recognitionError?.let { message ->
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
                ui = ui,
                enabled = onDeviceRecognitionAvailable && !isListening,
                listening = isListening && activeLessonId == speakingId,
                onSpeak = { consumeResults ->
                    startSpeakingLesson(
                        speakingId = speakingId,
                        consumeResults = consumeResults,
                    )
                },
                onCompleted = {
                    onLessonCompleted(speakingId)
                },
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
    ui: SpeakingUiText,
    enabled: Boolean,
    listening: Boolean,
    onSpeak: ((List<String>) -> Unit) -> Unit,
    onCompleted: () -> Unit,
) {
    var recognizedText by remember(lesson.id) { mutableStateOf<String?>(null) }
    var similarity by remember(lesson.id) { mutableStateOf<Float?>(null) }

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
                onClick = {
                    recognizedText = null
                    similarity = null

                    onSpeak { candidates ->
                        val scored = candidates.map { candidate ->
                            candidate to koreanSimilarity(candidate, lesson.korean)
                        }
                        val best = scored.maxByOrNull { it.second }
                        recognizedText = best?.first
                        similarity = best?.second ?: 0f

                        if ((best?.second ?: 0f) >= PASSING_SIMILARITY) {
                            onCompleted()
                        }
                    }
                },
            ) {
                Text(if (listening) ui.listeningLabel else ui.speakLabel)
            }

            recognizedText?.let { text ->
                Text(
                    text = "${ui.recognizedLabel}: $text",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            similarity?.let { score ->
                val percent = (score * 100).toInt()
                if (score >= PASSING_SIMILARITY) {
                    Text(
                        text = "${ui.matchedLabel} ($percent%)",
                        style = MaterialTheme.typography.titleMedium,
                    )
                } else {
                    Text(
                        text = "${ui.retryLabel} ($percent%)",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }

            if (similarity == null && completed) {
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
    value.lowercase(Locale.KOREAN)
        .filter { it.isLetterOrDigit() }

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
        intro = "Mira una frase útil, dilo en voz alta y compara lo que reconoció el dispositivo. La evaluación se hace con reconocimiento de voz coreano en el dispositivo.",
        targetLabel = "Di esta frase",
        speakLabel = "Hablar",
        listeningLabel = "Escuchando…",
        recognizedLabel = "Reconocido",
        matchedLabel = "¡Muy bien! La frase coincide.",
        retryLabel = "Casi. Inténtalo otra vez.",
        completedLabel = "Práctica oral completada",
        unavailableLabel = "Este dispositivo no tiene reconocimiento de voz coreano sin conexión disponible. Puedes continuar estudiando, pero la comprobación por voz queda desactivada.",
        permissionDeniedLabel = "Se necesita permiso de micrófono para practicar la pronunciación.",
        errorLabel = "No pude reconocer la voz. Inténtalo otra vez en un lugar más silencioso.",
        continueLabel = "Continuar a conversación",
    )

    "fr" -> SpeakingUiText(
        title = "Parler coréen",
        intro = "Lis une phrase utile, dis-la à voix haute et compare ce que l'appareil a reconnu. L'évaluation utilise la reconnaissance vocale coréenne sur l'appareil.",
        targetLabel = "Dis cette phrase",
        speakLabel = "Parler",
        listeningLabel = "Écoute…",
        recognizedLabel = "Reconnu",
        matchedLabel = "Très bien ! La phrase correspond.",
        retryLabel = "Presque. Réessaie.",
        completedLabel = "Expression orale terminée",
        unavailableLabel = "La reconnaissance vocale coréenne hors ligne n'est pas disponible sur cet appareil. Tu peux continuer à apprendre, mais la vérification vocale est désactivée.",
        permissionDeniedLabel = "L'autorisation du microphone est nécessaire pour pratiquer la prononciation.",
        errorLabel = "La voix n'a pas été reconnue. Réessaie dans un endroit plus calme.",
        continueLabel = "Continuer vers la conversation",
    )

    "vi" -> SpeakingUiText(
        title = "Nói tiếng Hàn",
        intro = "Xem một câu hữu ích, nói thành tiếng rồi so sánh với kết quả thiết bị nhận dạng. Việc kiểm tra dùng nhận dạng giọng nói tiếng Hàn ngay trên thiết bị.",
        targetLabel = "Hãy nói câu này",
        speakLabel = "Nói",
        listeningLabel = "Đang nghe…",
        recognizedLabel = "Đã nhận dạng",
        matchedLabel = "Rất tốt! Câu nói khớp.",
        retryLabel = "Gần đúng rồi. Hãy thử lại.",
        completedLabel = "Đã hoàn thành luyện nói",
        unavailableLabel = "Thiết bị này không có nhận dạng giọng nói tiếng Hàn ngoại tuyến. Bạn vẫn có thể học tiếp nhưng chức năng kiểm tra giọng nói sẽ bị tắt.",
        permissionDeniedLabel = "Cần quyền sử dụng micrô để luyện phát âm.",
        errorLabel = "Không nhận dạng được giọng nói. Hãy thử lại ở nơi yên tĩnh hơn.",
        continueLabel = "Tiếp tục sang hội thoại",
    )

    "th" -> SpeakingUiText(
        title = "พูดภาษาเกาหลี",
        intro = "ดูประโยคที่ใช้จริง พูดออกเสียง แล้วเปรียบเทียบกับข้อความที่อุปกรณ์ฟังได้ การตรวจใช้การรู้จำเสียงภาษาเกาหลีบนอุปกรณ์",
        targetLabel = "พูดประโยคนี้",
        speakLabel = "พูด",
        listeningLabel = "กำลังฟัง…",
        recognizedLabel = "ฟังได้ว่า",
        matchedLabel = "ดีมาก! ประโยคตรงกัน",
        retryLabel = "เกือบแล้ว ลองอีกครั้ง",
        completedLabel = "ฝึกพูดเสร็จแล้ว",
        unavailableLabel = "อุปกรณ์นี้ไม่มีการรู้จำเสียงภาษาเกาหลีแบบออฟไลน์ คุณยังเรียนต่อได้ แต่การตรวจเสียงจะถูกปิด",
        permissionDeniedLabel = "ต้องอนุญาตใช้ไมโครโฟนเพื่อฝึกออกเสียง",
        errorLabel = "ไม่สามารถรู้จำเสียงได้ ลองอีกครั้งในที่เงียบกว่า",
        continueLabel = "ไปฝึกสนทนา",
    )

    "id" -> SpeakingUiText(
        title = "Berbicara bahasa Korea",
        intro = "Lihat kalimat yang berguna, ucapkan dengan suara keras, lalu bandingkan hasil yang dikenali perangkat. Penilaian memakai pengenal suara Korea di perangkat.",
        targetLabel = "Ucapkan kalimat ini",
        speakLabel = "Bicara",
        listeningLabel = "Mendengarkan…",
        recognizedLabel = "Dikenali",
        matchedLabel = "Bagus! Kalimatnya cocok.",
        retryLabel = "Hampir. Coba lagi.",
        completedLabel = "Latihan berbicara selesai",
        unavailableLabel = "Pengenalan suara Korea offline tidak tersedia di perangkat ini. Anda tetap dapat belajar, tetapi pemeriksaan suara dinonaktifkan.",
        permissionDeniedLabel = "Izin mikrofon diperlukan untuk latihan pengucapan.",
        errorLabel = "Suara tidak dapat dikenali. Coba lagi di tempat yang lebih tenang.",
        continueLabel = "Lanjut ke percakapan",
    )

    else -> SpeakingUiText(
        title = "Speak Korean",
        intro = "Read a useful sentence, say it aloud, and compare what your device recognized. Scoring uses Korean on-device speech recognition.",
        targetLabel = "Say this sentence",
        speakLabel = "Speak",
        listeningLabel = "Listening…",
        recognizedLabel = "Recognized",
        matchedLabel = "Great! Your sentence matched.",
        retryLabel = "Almost. Try it again.",
        completedLabel = "Speaking completed",
        unavailableLabel = "Offline Korean speech recognition is not available on this device. You can keep learning, but voice checking is disabled.",
        permissionDeniedLabel = "Microphone permission is required for speaking practice.",
        errorLabel = "I could not recognize the speech. Try again somewhere quieter.",
        continueLabel = "Continue to conversation",
    )
}
