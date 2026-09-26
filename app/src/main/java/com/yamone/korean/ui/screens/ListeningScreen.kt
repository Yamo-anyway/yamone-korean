package com.yamone.korean.ui.screens

import android.speech.tts.TextToSpeech
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
import com.yamone.korean.learning.BasicSentenceCatalog
import com.yamone.korean.learning.BasicSentenceLesson
import java.util.Locale

private data class ListeningUiText(
    val title: String,
    val intro: String,
    val slowLabel: String,
    val naturalLabel: String,
    val chooseLabel: String,
    val correctLabel: String,
    val retryLabel: String,
    val completedLabel: String,
    val unavailableLabel: String,
    val continueLabel: String,
)

@Composable
fun ListeningScreen(
    languageCode: String?,
    completedLessonIds: Set<String>,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val ui = listeningUiText(languageCode)
    var ttsReady by remember { mutableStateOf(false) }
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }

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

    fun speak(text: String, rate: Float, utteranceId: String) {
        val engine = ttsEngine ?: return
        if (!ttsReady) return
        engine.setSpeechRate(rate)
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
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
                    text = ui.unavailableLabel,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        items(
            items = BasicSentenceCatalog.lessons,
            key = { it.id },
        ) { lesson ->
            val listeningId = "listening_${lesson.id}"
            ListeningLessonCard(
                lesson = lesson,
                completed = listeningId in completedLessonIds,
                ui = ui,
                audioEnabled = ttsReady,
                onSlow = {
                    onLessonOpened(listeningId)
                    speak(lesson.korean, 0.72f, "${listeningId}_slow")
                },
                onNatural = {
                    onLessonOpened(listeningId)
                    speak(lesson.korean, 1.0f, "${listeningId}_natural")
                },
                onCompleted = { onLessonCompleted(listeningId) },
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
private fun ListeningLessonCard(
    lesson: BasicSentenceLesson,
    completed: Boolean,
    ui: ListeningUiText,
    audioEnabled: Boolean,
    onSlow: () -> Unit,
    onNatural: () -> Unit,
    onCompleted: () -> Unit,
) {
    var selectedAnswer by remember(lesson.id) { mutableStateOf<String?>(null) }
    var result by remember(lesson.id) { mutableStateOf<Boolean?>(null) }

    val allSentences = BasicSentenceCatalog.lessons.map { it.korean }
    val distractors = allSentences
        .filterNot { it == lesson.korean }
        .shuffled(java.util.Random(lesson.id.hashCode().toLong()))
        .take(2)
    val answers = remember(lesson.id) {
        (distractors + lesson.korean)
            .shuffled(java.util.Random((lesson.id.hashCode() * 31L) + 7L))
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = lesson.meaning(null),
                style = MaterialTheme.typography.labelLarge,
            )

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = audioEnabled,
                onClick = onSlow,
            ) {
                Text(ui.slowLabel)
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = audioEnabled,
                onClick = onNatural,
            ) {
                Text(ui.naturalLabel)
            }

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = ui.chooseLabel,
                style = MaterialTheme.typography.titleSmall,
            )

            answers.forEach { answer ->
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = result != true,
                    onClick = {
                        selectedAnswer = answer
                        val isCorrect = answer == lesson.korean
                        result = isCorrect
                        if (isCorrect) onCompleted()
                    },
                ) {
                    Text(answer)
                }
            }

            when (result) {
                true -> Text(
                    text = ui.correctLabel,
                    style = MaterialTheme.typography.titleMedium,
                )

                false -> Text(
                    text = ui.retryLabel,
                    style = MaterialTheme.typography.titleMedium,
                )

                null -> if (completed) {
                    Text(
                        text = ui.completedLabel,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            if (selectedAnswer != null && result != true) {
                Text(
                    text = selectedAnswer.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

private fun listeningUiText(languageCode: String?): ListeningUiText = when (languageCode) {
    "es" -> ListeningUiText(
        title = "Escucha coreano real",
        intro = "Escucha primero sin mirar la respuesta. Empieza despacio, luego escucha a velocidad natural y elige la frase que oíste.",
        slowLabel = "Escuchar despacio",
        naturalLabel = "Escuchar natural",
        chooseLabel = "¿Qué frase escuchaste?",
        correctLabel = "¡Correcto! Reconociste la frase.",
        retryLabel = "Todavía no. Escúchala otra vez.",
        completedLabel = "Escucha completada",
        unavailableLabel = "La voz coreana no está instalada en este dispositivo. Instala una voz coreana sin conexión para usar el audio.",
        continueLabel = "Continuar a hablar",
    )

    "fr" -> ListeningUiText(
        title = "Écouter du coréen réel",
        intro = "Écoute d'abord sans regarder la réponse. Commence lentement, puis à vitesse naturelle, et choisis la phrase entendue.",
        slowLabel = "Écouter lentement",
        naturalLabel = "Écouter naturellement",
        chooseLabel = "Quelle phrase as-tu entendue ?",
        correctLabel = "Correct ! Tu as reconnu la phrase.",
        retryLabel = "Pas encore. Écoute encore une fois.",
        completedLabel = "Écoute terminée",
        unavailableLabel = "La voix coréenne n'est pas installée sur cet appareil. Installe une voix coréenne hors ligne pour utiliser l'audio.",
        continueLabel = "Continuer vers l'oral",
    )

    "vi" -> ListeningUiText(
        title = "Nghe tiếng Hàn thực tế",
        intro = "Hãy nghe trước khi nhìn đáp án. Bắt đầu với tốc độ chậm, sau đó nghe tốc độ tự nhiên và chọn câu bạn vừa nghe.",
        slowLabel = "Nghe chậm",
        naturalLabel = "Nghe tự nhiên",
        chooseLabel = "Bạn đã nghe câu nào?",
        correctLabel = "Đúng rồi! Bạn đã nhận ra câu.",
        retryLabel = "Chưa đúng. Hãy nghe lại.",
        completedLabel = "Đã hoàn thành nghe",
        unavailableLabel = "Thiết bị chưa có giọng tiếng Hàn. Hãy cài giọng tiếng Hàn ngoại tuyến để dùng âm thanh.",
        continueLabel = "Tiếp tục sang nói",
    )

    "th" -> ListeningUiText(
        title = "ฟังภาษาเกาหลีที่ใช้จริง",
        intro = "ฟังก่อนโดยไม่ดูคำตอบ เริ่มจากความเร็วช้า แล้วฟังความเร็วธรรมชาติ จากนั้นเลือกประโยคที่ได้ยิน",
        slowLabel = "ฟังแบบช้า",
        naturalLabel = "ฟังแบบธรรมชาติ",
        chooseLabel = "คุณได้ยินประโยคไหน?",
        correctLabel = "ถูกต้อง! คุณฟังออกแล้ว",
        retryLabel = "ยังไม่ถูก ลองฟังอีกครั้ง",
        completedLabel = "ฝึกฟังแล้ว",
        unavailableLabel = "อุปกรณ์นี้ยังไม่มีเสียงภาษาเกาหลี โปรดติดตั้งเสียงภาษาเกาหลีแบบออฟไลน์เพื่อใช้เสียง",
        continueLabel = "ไปฝึกพูด",
    )

    "id" -> ListeningUiText(
        title = "Dengarkan bahasa Korea nyata",
        intro = "Dengarkan dulu tanpa melihat jawaban. Mulai dengan kecepatan lambat, lalu kecepatan alami, kemudian pilih kalimat yang Anda dengar.",
        slowLabel = "Dengar perlahan",
        naturalLabel = "Dengar alami",
        chooseLabel = "Kalimat mana yang Anda dengar?",
        correctLabel = "Benar! Anda mengenali kalimatnya.",
        retryLabel = "Belum benar. Dengarkan sekali lagi.",
        completedLabel = "Latihan mendengar selesai",
        unavailableLabel = "Suara Korea belum terpasang di perangkat ini. Pasang suara Korea offline untuk menggunakan audio.",
        continueLabel = "Lanjut ke berbicara",
    )

    else -> ListeningUiText(
        title = "Listen to real Korean",
        intro = "Listen before looking for the answer. Start slow, then hear the same sentence at natural speed and choose what you heard.",
        slowLabel = "Listen slowly",
        naturalLabel = "Listen naturally",
        chooseLabel = "Which sentence did you hear?",
        correctLabel = "Correct! You recognized the sentence.",
        retryLabel = "Not yet. Listen one more time.",
        completedLabel = "Listening completed",
        unavailableLabel = "A Korean voice is not installed on this device. Install an offline Korean voice to use audio.",
        continueLabel = "Continue to speaking",
    )
}
