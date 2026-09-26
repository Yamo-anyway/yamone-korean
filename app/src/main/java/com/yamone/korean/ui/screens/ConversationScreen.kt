package com.yamone.korean.ui.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
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
import androidx.compose.material3.OutlinedButton
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
import com.yamone.korean.learning.ConversationCatalog
import com.yamone.korean.learning.ConversationLesson
import com.yamone.korean.learning.ConversationReply
import java.util.Locale
import kotlin.math.max

private const val CONVERSATION_PASSING_SIMILARITY = 0.70f

private data class ConversationUiText(
    val title: String,
    val intro: String,
    val partnerLabel: String,
    val listenLabel: String,
    val chooseLabel: String,
    val myReplyLabel: String,
    val speakLabel: String,
    val recognizedLabel: String,
    val passedLabel: String,
    val retryLabel: String,
    val manualLabel: String,
    val partnerReplyLabel: String,
    val hearReplyLabel: String,
    val completedLabel: String,
    val speechUnavailableLabel: String,
    val audioUnavailableLabel: String,
    val canceledLabel: String,
    val continueLabel: String,
)

private data class ConversationAttempt(
    val recognizedText: String,
    val similarity: Float,
)

@Composable
fun ConversationScreen(
    languageCode: String?,
    completedLessonIds: Set<String>,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val ui = conversationUiText(languageCode)
    val speechAvailable = remember(context) {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
    }

    var ttsReady by remember { mutableStateOf(false) }
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var activeLesson by remember { mutableStateOf<ConversationLesson?>(null) }
    var activeReply by remember { mutableStateOf<ConversationReply?>(null) }
    var attempts by remember { mutableStateOf<Map<String, ConversationAttempt>>(emptyMap()) }
    var readyForClosing by remember { mutableStateOf<Set<String>>(emptySet()) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    DisposableEffect(context) {
        lateinit var engine: TextToSpeech
        engine = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val languageResult = engine.setLanguage(Locale.KOREAN)
                ttsReady =
                    languageResult != TextToSpeech.LANG_MISSING_DATA &&
                        languageResult != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
        ttsEngine = engine

        onDispose {
            engine.stop()
            engine.shutdown()
            ttsEngine = null
        }
    }

    fun speak(text: String, utteranceId: String) {
        val engine = ttsEngine ?: return
        if (!ttsReady) return
        engine.setSpeechRate(1.0f)
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        val lesson = activeLesson
        val reply = activeReply
        if (lesson == null || reply == null) return@rememberLauncherForActivityResult

        val lessonId = "conversation_${lesson.id}"
        if (result.resultCode != Activity.RESULT_OK) {
            statusMessage = ui.canceledLabel
            activeLesson = null
            activeReply = null
            return@rememberLauncherForActivityResult
        }

        val candidates = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            .orEmpty()

        val best = candidates
            .map { candidate -> candidate to conversationSimilarity(candidate, reply.korean) }
            .maxByOrNull { it.second }

        val similarity = best?.second ?: 0f
        attempts = attempts + (
            lessonId to ConversationAttempt(
                recognizedText = best?.first.orEmpty(),
                similarity = similarity,
            )
        )

        if (similarity >= CONVERSATION_PASSING_SIMILARITY) {
            readyForClosing = readyForClosing + lessonId
        }

        activeLesson = null
        activeReply = null
    }

    fun startSpeech(lesson: ConversationLesson, reply: ConversationReply) {
        val lessonId = "conversation_${lesson.id}"
        onLessonOpened(lessonId)
        activeLesson = lesson
        activeReply = reply
        statusMessage = null

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            putExtra(RecognizerIntent.EXTRA_PROMPT, reply.korean)
        }

        try {
            speechLauncher.launch(intent)
        } catch (_: ActivityNotFoundException) {
            statusMessage = ui.speechUnavailableLabel
            activeLesson = null
            activeReply = null
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
            if (!ttsReady) {
                Text(
                    text = ui.audioUnavailableLabel,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            if (!speechAvailable) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = ui.speechUnavailableLabel,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            statusMessage?.let {
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        items(
            items = ConversationCatalog.lessons,
            key = { it.id },
        ) { lesson ->
            val lessonId = "conversation_${lesson.id}"
            ConversationLessonCard(
                lesson = lesson,
                languageCode = languageCode,
                ui = ui,
                completed = lessonId in completedLessonIds,
                attempt = attempts[lessonId],
                readyForClosing = lessonId in readyForClosing || lessonId in completedLessonIds,
                speechEnabled = speechAvailable,
                audioEnabled = ttsReady,
                onLessonOpened = { onLessonOpened(lessonId) },
                onListenPartner = {
                    onLessonOpened(lessonId)
                    speak(lesson.partnerLine, "${lessonId}_partner")
                },
                onSpeak = { reply -> startSpeech(lesson, reply) },
                onManualReady = {
                    onLessonOpened(lessonId)
                    readyForClosing = readyForClosing + lessonId
                },
                onHearClosing = {
                    speak(lesson.closingLine, "${lessonId}_closing")
                    onLessonCompleted(lessonId)
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
private fun ConversationLessonCard(
    lesson: ConversationLesson,
    languageCode: String?,
    ui: ConversationUiText,
    completed: Boolean,
    attempt: ConversationAttempt?,
    readyForClosing: Boolean,
    speechEnabled: Boolean,
    audioEnabled: Boolean,
    onLessonOpened: () -> Unit,
    onListenPartner: () -> Unit,
    onSpeak: (ConversationReply) -> Unit,
    onManualReady: () -> Unit,
    onHearClosing: () -> Unit,
) {
    var selectedReplyId by remember(lesson.id) { mutableStateOf<String?>(null) }
    val selectedReply = lesson.replies.firstOrNull { it.id == selectedReplyId }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = lesson.situation(languageCode),
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = ui.partnerLabel,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = lesson.partnerLine,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = lesson.partnerMeaning(languageCode),
                style = MaterialTheme.typography.bodyMedium,
            )

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = audioEnabled,
                onClick = onListenPartner,
            ) {
                Text(ui.listenLabel)
            }

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = ui.chooseLabel,
                style = MaterialTheme.typography.titleSmall,
            )

            lesson.replies.forEach { reply ->
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        selectedReplyId = reply.id
                        onLessonOpened()
                    },
                ) {
                    Text("${reply.korean} · ${reply.meaning(languageCode)}")
                }
            }

            selectedReply?.let { reply ->
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = ui.myReplyLabel,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = reply.korean,
                    style = MaterialTheme.typography.headlineSmall,
                )

                if (speechEnabled) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSpeak(reply) },
                    ) {
                        Text(ui.speakLabel)
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onManualReady,
                    ) {
                        Text(ui.manualLabel)
                    }
                }
            }

            attempt?.let { value ->
                Text(
                    text = "${ui.recognizedLabel}: ${value.recognizedText}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = if (value.similarity >= CONVERSATION_PASSING_SIMILARITY) {
                        ui.passedLabel
                    } else {
                        ui.retryLabel
                    },
                    style = MaterialTheme.typography.titleSmall,
                )
            }

            if (readyForClosing) {
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = ui.partnerReplyLabel,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = lesson.closingLine,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = lesson.closingMeaning(languageCode),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onHearClosing,
                ) {
                    Text(ui.hearReplyLabel)
                }
            }

            if (completed) {
                Text(
                    text = ui.completedLabel,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

private fun conversationSimilarity(recognized: String, target: String): Float {
    val left = normalizeConversationSpeech(recognized)
    val right = normalizeConversationSpeech(target)
    if (left.isEmpty() || right.isEmpty()) return 0f
    if (left == right) return 1f

    val distance = conversationLevenshteinDistance(left, right)
    return 1f - (distance.toFloat() / max(left.length, right.length).toFloat())
}

private fun normalizeConversationSpeech(value: String): String =
    value.lowercase(Locale.KOREAN).filter { it.isLetterOrDigit() }

private fun conversationLevenshteinDistance(left: String, right: String): Int {
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

private fun conversationUiText(languageCode: String?): ConversationUiText = when (languageCode) {
    "es" -> ConversationUiText(
        title = "Conversaciones cortas",
        intro = "Escucha una pregunta real, elige tu respuesta, dila en coreano y escucha la respuesta de la otra persona.",
        partnerLabel = "La otra persona",
        listenLabel = "Escuchar la pregunta",
        chooseLabel = "Elige cómo responder",
        myReplyLabel = "Mi respuesta",
        speakLabel = "Decir mi respuesta",
        recognizedLabel = "Reconocido",
        passedLabel = "Bien. La respuesta se entendió.",
        retryLabel = "Casi. Mira la respuesta y dilo otra vez.",
        manualLabel = "Lo dije en voz alta",
        partnerReplyLabel = "Respuesta de la otra persona",
        hearReplyLabel = "Escuchar y completar",
        completedLabel = "Conversación completada",
        speechUnavailableLabel = "El reconocimiento coreano sin conexión no está disponible. Puedes practicar hablando y continuar manualmente.",
        audioUnavailableLabel = "La voz coreana no está instalada. Instala una voz coreana sin conexión para escuchar el diálogo.",
        canceledLabel = "La escucha se canceló. Inténtalo otra vez cuando quieras.",
        continueLabel = "Continuar a repaso",
    )

    "fr" -> ConversationUiText(
        title = "Courtes conversations",
        intro = "Écoute une vraie question, choisis ta réponse, dis-la en coréen puis écoute la réponse de l'autre personne.",
        partnerLabel = "Interlocuteur",
        listenLabel = "Écouter la question",
        chooseLabel = "Choisis ta réponse",
        myReplyLabel = "Ma réponse",
        speakLabel = "Dire ma réponse",
        recognizedLabel = "Reconnu",
        passedLabel = "Bien. Ta réponse a été comprise.",
        retryLabel = "Presque. Regarde la réponse et redis-la.",
        manualLabel = "Je l'ai dite à voix haute",
        partnerReplyLabel = "Réponse de l'interlocuteur",
        hearReplyLabel = "Écouter et terminer",
        completedLabel = "Conversation terminée",
        speechUnavailableLabel = "La reconnaissance coréenne hors ligne n'est pas disponible. Parle à voix haute puis continue manuellement.",
        audioUnavailableLabel = "La voix coréenne n'est pas installée. Installe une voix coréenne hors ligne pour écouter le dialogue.",
        canceledLabel = "L'écoute a été annulée. Réessaie quand tu veux.",
        continueLabel = "Continuer vers la révision",
    )

    "vi" -> ConversationUiText(
        title = "Hội thoại ngắn",
        intro = "Nghe một câu hỏi thực tế, chọn câu trả lời, nói bằng tiếng Hàn rồi nghe phản hồi của người đối diện.",
        partnerLabel = "Người đối diện",
        listenLabel = "Nghe câu hỏi",
        chooseLabel = "Chọn cách trả lời",
        myReplyLabel = "Câu trả lời của tôi",
        speakLabel = "Nói câu trả lời",
        recognizedLabel = "Đã nhận dạng",
        passedLabel = "Tốt. Câu trả lời đã được nhận ra.",
        retryLabel = "Gần đúng rồi. Nhìn câu và nói lại.",
        manualLabel = "Tôi đã nói thành tiếng",
        partnerReplyLabel = "Phản hồi của người đối diện",
        hearReplyLabel = "Nghe và hoàn thành",
        completedLabel = "Đã hoàn thành hội thoại",
        speechUnavailableLabel = "Không có nhận dạng tiếng Hàn ngoại tuyến. Hãy nói thành tiếng rồi tiếp tục thủ công.",
        audioUnavailableLabel = "Thiết bị chưa có giọng tiếng Hàn. Hãy cài giọng ngoại tuyến để nghe hội thoại.",
        canceledLabel = "Đã hủy nghe. Bạn có thể thử lại bất cứ lúc nào.",
        continueLabel = "Tiếp tục sang ôn tập",
    )

    "th" -> ConversationUiText(
        title = "บทสนทนาสั้น ๆ",
        intro = "ฟังคำถามจริง เลือกคำตอบ พูดเป็นภาษาเกาหลี แล้วฟังคำตอบของอีกฝ่าย",
        partnerLabel = "คู่สนทนา",
        listenLabel = "ฟังคำถาม",
        chooseLabel = "เลือกวิธีตอบ",
        myReplyLabel = "คำตอบของฉัน",
        speakLabel = "พูดคำตอบ",
        recognizedLabel = "ฟังได้ว่า",
        passedLabel = "ดีมาก ระบบเข้าใจคำตอบแล้ว",
        retryLabel = "เกือบแล้ว ดูประโยคแล้วลองพูดอีกครั้ง",
        manualLabel = "ฉันพูดออกเสียงแล้ว",
        partnerReplyLabel = "คำตอบของคู่สนทนา",
        hearReplyLabel = "ฟังและจบบทสนทนา",
        completedLabel = "จบบทสนทนาแล้ว",
        speechUnavailableLabel = "ไม่มีการรู้จำภาษาเกาหลีแบบออฟไลน์ ให้พูดออกเสียงแล้วดำเนินต่อด้วยตนเอง",
        audioUnavailableLabel = "ยังไม่มีเสียงภาษาเกาหลีในอุปกรณ์ โปรดติดตั้งเสียงแบบออฟไลน์เพื่อฟังบทสนทนา",
        canceledLabel = "ยกเลิกการฟังแล้ว ลองใหม่ได้ทุกเมื่อ",
        continueLabel = "ไปทบทวน",
    )

    "id" -> ConversationUiText(
        title = "Percakapan singkat",
        intro = "Dengarkan pertanyaan nyata, pilih jawaban, ucapkan dalam bahasa Korea, lalu dengarkan balasan lawan bicara.",
        partnerLabel = "Lawan bicara",
        listenLabel = "Dengarkan pertanyaan",
        chooseLabel = "Pilih cara menjawab",
        myReplyLabel = "Jawaban saya",
        speakLabel = "Ucapkan jawaban",
        recognizedLabel = "Dikenali",
        passedLabel = "Bagus. Jawaban Anda dikenali.",
        retryLabel = "Hampir. Lihat jawabannya dan ucapkan lagi.",
        manualLabel = "Saya sudah mengucapkannya",
        partnerReplyLabel = "Balasan lawan bicara",
        hearReplyLabel = "Dengar dan selesaikan",
        completedLabel = "Percakapan selesai",
        speechUnavailableLabel = "Pengenalan bahasa Korea offline tidak tersedia. Berlatihlah dengan suara keras lalu lanjutkan secara manual.",
        audioUnavailableLabel = "Suara Korea belum terpasang. Pasang suara Korea offline untuk mendengarkan dialog.",
        canceledLabel = "Pengenalan suara dibatalkan. Coba lagi kapan saja.",
        continueLabel = "Lanjut ke ulasan",
    )

    else -> ConversationUiText(
        title = "Short real conversations",
        intro = "Hear a real question, choose your reply, say it in Korean, then listen to the other person's response.",
        partnerLabel = "Partner",
        listenLabel = "Listen to the question",
        chooseLabel = "Choose how to reply",
        myReplyLabel = "My reply",
        speakLabel = "Say my reply",
        recognizedLabel = "Recognized",
        passedLabel = "Good. Your reply was understood.",
        retryLabel = "Almost. Look at the reply and say it again.",
        manualLabel = "I said it aloud",
        partnerReplyLabel = "Partner reply",
        hearReplyLabel = "Listen and complete",
        completedLabel = "Conversation completed",
        speechUnavailableLabel = "Offline Korean recognition is not available. Practice aloud and continue manually.",
        audioUnavailableLabel = "A Korean voice is not installed. Install an offline Korean voice to hear the dialogue.",
        canceledLabel = "Listening was canceled. Try again whenever you are ready.",
        continueLabel = "Continue to review",
    )
}
