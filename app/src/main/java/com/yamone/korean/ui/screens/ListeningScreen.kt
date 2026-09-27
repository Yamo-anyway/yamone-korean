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
    focusSentenceId: String? = null,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onLessonNeedsReview: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val context = LocalContext.current
    val ui = listeningUiText(languageCode)
    val orderedLessons = remember(focusSentenceId) {
        if (focusSentenceId == null) {
            BasicSentenceCatalog.lessons
        } else {
            BasicSentenceCatalog.lessons.sortedBy { lesson ->
                if (lesson.id == focusSentenceId) 0 else 1
            }
        }
    }
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
            items = orderedLessons,
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
                onNeedsReview = { onLessonNeedsReview(listeningId) },
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
    onNeedsReview: () -> Unit,
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
                        if (isCorrect) {
                            onCompleted()
                        } else {
                            onNeedsReview()
                        }
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
        intro = "Escucha primero sin mirar la respuesta. Empieza despacio, luego escucha a velocidad natural y elige la frase que o\u00edste.",
        slowLabel = "Escuchar despacio",
        naturalLabel = "Escuchar natural",
        chooseLabel = "\u00bfQu\u00e9 frase escuchaste?",
        correctLabel = "\u00a1Correcto! Reconociste la frase.",
        retryLabel = "Todav\u00eda no. Esc\u00fachala otra vez.",
        completedLabel = "Escucha completada",
        unavailableLabel = "La voz coreana no est\u00e1 instalada en este dispositivo. Instala una voz coreana sin conexi\u00f3n para usar el audio.",
        continueLabel = "Continuar a hablar",
    )

    "fr" -> ListeningUiText(
        title = "\u00c9couter du cor\u00e9en r\u00e9el",
        intro = "\u00c9coute d'abord sans regarder la r\u00e9ponse. Commence lentement, puis \u00e0 vitesse naturelle, et choisis la phrase entendue.",
        slowLabel = "\u00c9couter lentement",
        naturalLabel = "\u00c9couter naturellement",
        chooseLabel = "Quelle phrase as-tu entendue ?",
        correctLabel = "Correct ! Tu as reconnu la phrase.",
        retryLabel = "Pas encore. \u00c9coute encore une fois.",
        completedLabel = "\u00c9coute termin\u00e9e",
        unavailableLabel = "La voix cor\u00e9enne n'est pas install\u00e9e sur cet appareil. Installe une voix cor\u00e9enne hors ligne pour utiliser l'audio.",
        continueLabel = "Continuer vers l'oral",
    )

    "vi" -> ListeningUiText(
        title = "Nghe ti\u1ebfng H\u00e0n th\u1ef1c t\u1ebf",
        intro = "H\u00e3y nghe tr\u01b0\u1edbc khi nh\u00ecn \u0111\u00e1p \u00e1n. B\u1eaft \u0111\u1ea7u v\u1edbi t\u1ed1c \u0111\u1ed9 ch\u1eadm, sau \u0111\u00f3 nghe t\u1ed1c \u0111\u1ed9 t\u1ef1 nhi\u00ean v\u00e0 ch\u1ecdn c\u00e2u b\u1ea1n v\u1eeba nghe.",
        slowLabel = "Nghe ch\u1eadm",
        naturalLabel = "Nghe t\u1ef1 nhi\u00ean",
        chooseLabel = "B\u1ea1n \u0111\u00e3 nghe c\u00e2u n\u00e0o?",
        correctLabel = "\u0110\u00fang r\u1ed3i! B\u1ea1n \u0111\u00e3 nh\u1eadn ra c\u00e2u.",
        retryLabel = "Ch\u01b0a \u0111\u00fang. H\u00e3y nghe l\u1ea1i.",
        completedLabel = "\u0110\u00e3 ho\u00e0n th\u00e0nh nghe",
        unavailableLabel = "Thi\u1ebft b\u1ecb ch\u01b0a c\u00f3 gi\u1ecdng ti\u1ebfng H\u00e0n. H\u00e3y c\u00e0i gi\u1ecdng ti\u1ebfng H\u00e0n ngo\u1ea1i tuy\u1ebfn \u0111\u1ec3 d\u00f9ng \u00e2m thanh.",
        continueLabel = "Ti\u1ebfp t\u1ee5c sang n\u00f3i",
    )

    "th" -> ListeningUiText(
        title = "\u0e1f\u0e31\u0e07\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e17\u0e35\u0e48\u0e43\u0e0a\u0e49\u0e08\u0e23\u0e34\u0e07",
        intro = "\u0e1f\u0e31\u0e07\u0e01\u0e48\u0e2d\u0e19\u0e42\u0e14\u0e22\u0e44\u0e21\u0e48\u0e14\u0e39\u0e04\u0e33\u0e15\u0e2d\u0e1a \u0e40\u0e23\u0e34\u0e48\u0e21\u0e08\u0e32\u0e01\u0e04\u0e27\u0e32\u0e21\u0e40\u0e23\u0e47\u0e27\u0e0a\u0e49\u0e32 \u0e41\u0e25\u0e49\u0e27\u0e1f\u0e31\u0e07\u0e04\u0e27\u0e32\u0e21\u0e40\u0e23\u0e47\u0e27\u0e18\u0e23\u0e23\u0e21\u0e0a\u0e32\u0e15\u0e34 \u0e08\u0e32\u0e01\u0e19\u0e31\u0e49\u0e19\u0e40\u0e25\u0e37\u0e2d\u0e01\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e17\u0e35\u0e48\u0e44\u0e14\u0e49\u0e22\u0e34\u0e19",
        slowLabel = "\u0e1f\u0e31\u0e07\u0e41\u0e1a\u0e1a\u0e0a\u0e49\u0e32",
        naturalLabel = "\u0e1f\u0e31\u0e07\u0e41\u0e1a\u0e1a\u0e18\u0e23\u0e23\u0e21\u0e0a\u0e32\u0e15\u0e34",
        chooseLabel = "\u0e04\u0e38\u0e13\u0e44\u0e14\u0e49\u0e22\u0e34\u0e19\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e44\u0e2b\u0e19?",
        correctLabel = "\u0e16\u0e39\u0e01\u0e15\u0e49\u0e2d\u0e07! \u0e04\u0e38\u0e13\u0e1f\u0e31\u0e07\u0e2d\u0e2d\u0e01\u0e41\u0e25\u0e49\u0e27",
        retryLabel = "\u0e22\u0e31\u0e07\u0e44\u0e21\u0e48\u0e16\u0e39\u0e01 \u0e25\u0e2d\u0e07\u0e1f\u0e31\u0e07\u0e2d\u0e35\u0e01\u0e04\u0e23\u0e31\u0e49\u0e07",
        completedLabel = "\u0e1d\u0e36\u0e01\u0e1f\u0e31\u0e07\u0e41\u0e25\u0e49\u0e27",
        unavailableLabel = "\u0e2d\u0e38\u0e1b\u0e01\u0e23\u0e13\u0e4c\u0e19\u0e35\u0e49\u0e22\u0e31\u0e07\u0e44\u0e21\u0e48\u0e21\u0e35\u0e40\u0e2a\u0e35\u0e22\u0e07\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35 \u0e42\u0e1b\u0e23\u0e14\u0e15\u0e34\u0e14\u0e15\u0e31\u0e49\u0e07\u0e40\u0e2a\u0e35\u0e22\u0e07\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e41\u0e1a\u0e1a\u0e2d\u0e2d\u0e1f\u0e44\u0e25\u0e19\u0e4c\u0e40\u0e1e\u0e37\u0e48\u0e2d\u0e43\u0e0a\u0e49\u0e40\u0e2a\u0e35\u0e22\u0e07",
        continueLabel = "\u0e44\u0e1b\u0e1d\u0e36\u0e01\u0e1e\u0e39\u0e14",
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
