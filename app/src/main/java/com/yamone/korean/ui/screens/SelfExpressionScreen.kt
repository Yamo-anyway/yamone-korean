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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.SelfExpressionCatalog
import com.yamone.korean.learning.SelfExpressionChoice
import com.yamone.korean.learning.SelfExpressionLesson
import java.util.Locale
import kotlin.math.max

private const val EXPRESSION_PASSING_SIMILARITY = 0.72f

private data class ExpressionUiText(
    val title: String,
    val intro: String,
    val patternLabel: String,
    val chooseLabel: String,
    val mySentenceLabel: String,
    val speakLabel: String,
    val recognizedLabel: String,
    val passedLabel: String,
    val retryLabel: String,
    val manualLabel: String,
    val completedLabel: String,
    val unavailableLabel: String,
    val canceledLabel: String,
    val continueLabel: String,
)

private data class ExpressionAttempt(
    val recognizedText: String,
    val similarity: Float,
)

@Composable
fun SelfExpressionScreen(
    languageCode: String?,
    completedLessonIds: Set<String>,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val ui = expressionUiText(languageCode)
    val onDeviceRecognitionAvailable = remember(context) {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            SpeechRecognizer.isOnDeviceRecognitionAvailable(context)
    }

    var activeLesson by remember { mutableStateOf<SelfExpressionLesson?>(null) }
    var activeChoice by remember { mutableStateOf<SelfExpressionChoice?>(null) }
    var attempts by remember { mutableStateOf<Map<String, ExpressionAttempt>>(emptyMap()) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        val lesson = activeLesson
        val choice = activeChoice
        if (lesson == null || choice == null) return@rememberLauncherForActivityResult

        val lessonId = "expression_${lesson.id}"
        if (result.resultCode != Activity.RESULT_OK) {
            statusMessage = ui.canceledLabel
            activeLesson = null
            activeChoice = null
            return@rememberLauncherForActivityResult
        }

        val candidates = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            .orEmpty()

        val best = candidates
            .map { candidate -> candidate to expressionSimilarity(candidate, choice.sentence) }
            .maxByOrNull { it.second }

        attempts = attempts + (
            lessonId to ExpressionAttempt(
                recognizedText = best?.first.orEmpty(),
                similarity = best?.second ?: 0f,
            )
        )

        if ((best?.second ?: 0f) >= EXPRESSION_PASSING_SIMILARITY) {
            onLessonCompleted(lessonId)
        }

        activeLesson = null
        activeChoice = null
    }

    fun startSpeech(lesson: SelfExpressionLesson, choice: SelfExpressionChoice) {
        val lessonId = "expression_${lesson.id}"
        onLessonOpened(lessonId)
        activeLesson = lesson
        activeChoice = choice
        statusMessage = null

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR")
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            putExtra(RecognizerIntent.EXTRA_PROMPT, choice.sentence)
        }

        try {
            speechLauncher.launch(intent)
        } catch (_: ActivityNotFoundException) {
            statusMessage = ui.unavailableLabel
            activeLesson = null
            activeChoice = null
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
            statusMessage?.let {
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        items(
            items = SelfExpressionCatalog.lessons,
            key = { it.id },
        ) { lesson ->
            val lessonId = "expression_${lesson.id}"
            SelfExpressionLessonCard(
                lesson = lesson,
                languageCode = languageCode,
                ui = ui,
                completed = lessonId in completedLessonIds,
                attempt = attempts[lessonId],
                speechEnabled = onDeviceRecognitionAvailable,
                onLessonOpened = { onLessonOpened(lessonId) },
                onSpeak = { choice -> startSpeech(lesson, choice) },
                onManualCompleted = { onLessonCompleted(lessonId) },
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
private fun SelfExpressionLessonCard(
    lesson: SelfExpressionLesson,
    languageCode: String?,
    ui: ExpressionUiText,
    completed: Boolean,
    attempt: ExpressionAttempt?,
    speechEnabled: Boolean,
    onLessonOpened: () -> Unit,
    onSpeak: (SelfExpressionChoice) -> Unit,
    onManualCompleted: () -> Unit,
) {
    var selectedChoiceId by remember(lesson.id) { mutableStateOf<String?>(null) }
    val selectedChoice = lesson.choices.firstOrNull { it.id == selectedChoiceId }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = ui.patternLabel,
                style = MaterialTheme.typography.labelMedium,
            )
            Text(
                text = lesson.template,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = lesson.prompt(languageCode),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = ui.chooseLabel,
                style = MaterialTheme.typography.titleSmall,
            )

            lesson.choices.forEach { choice ->
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        selectedChoiceId = choice.id
                        onLessonOpened()
                    },
                ) {
                    Text("${choice.slotText} · ${choice.meaning(languageCode)}")
                }
            }

            selectedChoice?.let { choice ->
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = ui.mySentenceLabel,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    text = choice.sentence,
                    style = MaterialTheme.typography.headlineSmall,
                )

                if (speechEnabled) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSpeak(choice) },
                    ) {
                        Text(ui.speakLabel)
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onManualCompleted,
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
                    text = if (value.similarity >= EXPRESSION_PASSING_SIMILARITY) {
                        ui.passedLabel
                    } else {
                        ui.retryLabel
                    },
                    style = MaterialTheme.typography.titleSmall,
                )
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

private fun expressionSimilarity(recognized: String, target: String): Float {
    val left = normalizeExpressionSpeech(recognized)
    val right = normalizeExpressionSpeech(target)
    if (left.isEmpty() || right.isEmpty()) return 0f
    if (left == right) return 1f

    val distance = expressionLevenshteinDistance(left, right)
    return 1f - (distance.toFloat() / max(left.length, right.length).toFloat())
}

private fun normalizeExpressionSpeech(value: String): String =
    value.lowercase(Locale.KOREAN).filter { it.isLetterOrDigit() }

private fun expressionLevenshteinDistance(left: String, right: String): Int {
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

private fun expressionUiText(languageCode: String?): ExpressionUiText = when (languageCode) {
    "es" -> ExpressionUiText(
        title = "Crea tu propia frase",
        intro = "Ya no repitas solo una respuesta fija. Elige algo que sea verdad para ti, construye la frase coreana y dilo en voz alta.",
        patternLabel = "Patrón",
        chooseLabel = "Elige tu palabra",
        mySentenceLabel = "Mi frase",
        speakLabel = "Decir mi frase",
        recognizedLabel = "Reconocido",
        passedLabel = "¡Bien! Has expresado tu propia idea en coreano.",
        retryLabel = "Casi. Mira la frase y vuelve a decirla.",
        manualLabel = "Lo dije en voz alta",
        completedLabel = "Expresión completada",
        unavailableLabel = "El reconocimiento coreano sin conexión no está disponible. Di la frase en voz alta y marca la práctica manualmente.",
        canceledLabel = "La escucha se canceló. Inténtalo otra vez cuando quieras.",
        continueLabel = "Continuar a preguntas y conversación",
    )

    "fr" -> ExpressionUiText(
        title = "Crée ta propre phrase",
        intro = "Ne répète plus seulement une réponse fixe. Choisis quelque chose qui te correspond, construis la phrase coréenne et dis-la à voix haute.",
        patternLabel = "Modèle",
        chooseLabel = "Choisis ton mot",
        mySentenceLabel = "Ma phrase",
        speakLabel = "Dire ma phrase",
        recognizedLabel = "Reconnu",
        passedLabel = "Bien ! Tu as exprimé ta propre idée en coréen.",
        retryLabel = "Presque. Regarde la phrase et dis-la encore.",
        manualLabel = "Je l'ai dite à voix haute",
        completedLabel = "Expression terminée",
        unavailableLabel = "La reconnaissance coréenne hors ligne n'est pas disponible. Dis la phrase à voix haute puis valide manuellement.",
        canceledLabel = "L'écoute a été annulée. Réessaie quand tu veux.",
        continueLabel = "Continuer vers les questions et la conversation",
    )

    "vi" -> ExpressionUiText(
        title = "Tạo câu của riêng bạn",
        intro = "Không chỉ lặp lại một đáp án cố định. Hãy chọn điều đúng với bạn, tạo câu tiếng Hàn rồi nói thành tiếng.",
        patternLabel = "Mẫu câu",
        chooseLabel = "Chọn từ của bạn",
        mySentenceLabel = "Câu của tôi",
        speakLabel = "Nói câu của tôi",
        recognizedLabel = "Đã nhận dạng",
        passedLabel = "Tốt lắm! Bạn đã diễn đạt ý của mình bằng tiếng Hàn.",
        retryLabel = "Gần đúng rồi. Nhìn câu và nói lại.",
        manualLabel = "Tôi đã nói thành tiếng",
        completedLabel = "Đã hoàn thành tự diễn đạt",
        unavailableLabel = "Không có nhận dạng tiếng Hàn ngoại tuyến. Hãy nói câu thành tiếng rồi tự đánh dấu hoàn thành.",
        canceledLabel = "Đã hủy nghe. Bạn có thể thử lại bất cứ lúc nào.",
        continueLabel = "Tiếp tục sang hỏi đáp và hội thoại",
    )

    "th" -> ExpressionUiText(
        title = "สร้างประโยคของคุณเอง",
        intro = "ไม่ใช่แค่พูดตามคำตอบเดิม เลือกสิ่งที่ตรงกับตัวคุณ สร้างประโยคเกาหลี แล้วพูดออกเสียง",
        patternLabel = "รูปแบบ",
        chooseLabel = "เลือกคำของคุณ",
        mySentenceLabel = "ประโยคของฉัน",
        speakLabel = "พูดประโยคของฉัน",
        recognizedLabel = "ฟังได้ว่า",
        passedLabel = "ดีมาก! คุณสื่อความคิดของตัวเองเป็นภาษาเกาหลีแล้ว",
        retryLabel = "เกือบแล้ว ดูประโยคแล้วลองพูดอีกครั้ง",
        manualLabel = "ฉันพูดออกเสียงแล้ว",
        completedLabel = "ฝึกสร้างประโยคเสร็จแล้ว",
        unavailableLabel = "ไม่มีการรู้จำภาษาเกาหลีแบบออฟไลน์ ให้พูดประโยคออกเสียงแล้วทำเครื่องหมายว่าเสร็จด้วยตนเอง",
        canceledLabel = "ยกเลิกการฟังแล้ว ลองใหม่ได้ทุกเมื่อ",
        continueLabel = "ไปฝึกถามตอบและสนทนา",
    )

    "id" -> ExpressionUiText(
        title = "Buat kalimat Anda sendiri",
        intro = "Jangan hanya mengulang jawaban tetap. Pilih sesuatu yang benar tentang diri Anda, buat kalimat Korea, lalu ucapkan.",
        patternLabel = "Pola",
        chooseLabel = "Pilih kata Anda",
        mySentenceLabel = "Kalimat saya",
        speakLabel = "Ucapkan kalimat saya",
        recognizedLabel = "Dikenali",
        passedLabel = "Bagus! Anda sudah menyampaikan ide sendiri dalam bahasa Korea.",
        retryLabel = "Hampir. Lihat kalimatnya dan ucapkan lagi.",
        manualLabel = "Saya sudah mengucapkannya",
        completedLabel = "Ekspresi selesai",
        unavailableLabel = "Pengenalan bahasa Korea offline tidak tersedia. Ucapkan kalimat dengan keras lalu tandai latihan secara manual.",
        canceledLabel = "Pengenalan suara dibatalkan. Coba lagi kapan saja.",
        continueLabel = "Lanjut ke tanya jawab dan percakapan",
    )

    else -> ExpressionUiText(
        title = "Make your own Korean",
        intro = "Stop repeating only fixed answers. Choose something that is true for you, build the Korean sentence, then say it aloud.",
        patternLabel = "Pattern",
        chooseLabel = "Choose your word",
        mySentenceLabel = "My sentence",
        speakLabel = "Say my sentence",
        recognizedLabel = "Recognized",
        passedLabel = "Great! You expressed your own idea in Korean.",
        retryLabel = "Almost. Look at the sentence and say it again.",
        manualLabel = "I said it aloud",
        completedLabel = "Self-expression completed",
        unavailableLabel = "Offline Korean recognition is not available. Say the sentence aloud, then mark the practice manually.",
        canceledLabel = "Listening was canceled. Try again whenever you are ready.",
        continueLabel = "Continue to questions and conversation",
    )
}
