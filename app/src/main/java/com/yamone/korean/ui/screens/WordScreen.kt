package com.yamone.korean.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicSentenceCatalog
import com.yamone.korean.learning.BasicWordCatalog
import com.yamone.korean.learning.BasicWordLesson
import com.yamone.korean.learning.GuidedPracticeCatalog
import com.yamone.korean.learning.GuidedPracticePack
import com.yamone.korean.learning.WordCategory

private data class WordUiText(
    val title: String,
    val intro: String,
    val showMeaning: String,
    val learned: String,
    val learnedDone: String,
    val guidedTitle: String,
    val guidedIntro: String,
    val knownWordsLabel: String,
    val practiceLabel: String,
    val continueLabel: String,
)

@Composable
fun WordScreen(
    languageCode: String?,
    completedLessonIds: Set<String>,
    onLessonOpened: (String) -> Unit,
    onLessonCompleted: (String) -> Unit,
    onStartGuidedPractice: (String) -> Unit,
    onContinue: () -> Unit,
) {
    val ui = wordUiText(languageCode)
    val revealed = remember { mutableStateMapOf<String, Boolean>() }
    val firstPendingLessonId = remember {
        BasicWordCatalog.lessons.firstOrNull { it.id !in completedLessonIds }?.id
    }
    val orderedLessons = remember(firstPendingLessonId) {
        if (firstPendingLessonId == null) {
            BasicWordCatalog.lessons
        } else {
            BasicWordCatalog.lessons.sortedBy { lesson ->
                if (lesson.id == firstPendingLessonId) 0 else 1
            }
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
        }

        items(
            items = orderedLessons,
            key = { it.id },
        ) { lesson ->
            WordLessonCard(
                lesson = lesson,
                languageCode = languageCode,
                meaningRevealed = revealed[lesson.id] == true,
                completed = lesson.id in completedLessonIds,
                ui = ui,
                onReveal = {
                    revealed[lesson.id] = true
                    onLessonOpened(lesson.id)
                },
                onCompleted = {
                    revealed[lesson.id] = true
                    onLessonCompleted(lesson.id)
                },
            )
        }

        item {
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = ui.guidedTitle,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = ui.guidedIntro,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        items(
            items = GuidedPracticeCatalog.packs,
            key = { "guided_" + it.id },
        ) { pack ->
            val sentence = BasicSentenceCatalog.lessons.firstOrNull { it.id == pack.sentenceId }
            if (sentence != null) {
                GuidedPracticePackCard(
                    pack = pack,
                    languageCode = languageCode,
                    sentence = sentence,
                    completedLessonIds = completedLessonIds,
                    ui = ui,
                    onStart = { onStartGuidedPractice(pack.sentenceId) },
                )
            }
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
private fun WordLessonCard(
    lesson: BasicWordLesson,
    languageCode: String?,
    meaningRevealed: Boolean,
    completed: Boolean,
    ui: WordUiText,
    onReveal: () -> Unit,
    onCompleted: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = categoryLabel(lesson.category, languageCode),
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = lesson.korean,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = lesson.syllables,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (meaningRevealed || completed) {
                Text(
                    text = lesson.meaning(languageCode),
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (!meaningRevealed && !completed) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onReveal,
                    ) {
                        Text(ui.showMeaning)
                    }
                } else {
                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = !completed,
                        onClick = onCompleted,
                    ) {
                        Text(if (completed) ui.learnedDone else ui.learned)
                    }
                }
            }
        }
    }
}

@Composable
private fun GuidedPracticePackCard(
    pack: GuidedPracticePack,
    languageCode: String?,
    sentence: com.yamone.korean.learning.BasicSentenceLesson,
    completedLessonIds: Set<String>,
    ui: WordUiText,
    onStart: () -> Unit,
) {
    val words = BasicWordCatalog.lessons.filter { it.id in pack.wordIds }
    val learnedCount = words.count { it.id in completedLessonIds }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = pack.title(languageCode), style = MaterialTheme.typography.titleMedium)
            Text(
                text = words.joinToString(" / ") { it.korean },
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "${ui.knownWordsLabel}: $learnedCount/${words.size}",
                style = MaterialTheme.typography.labelMedium,
            )
            Text(text = sentence.korean, style = MaterialTheme.typography.headlineSmall)
            Text(text = sentence.meaning(languageCode), style = MaterialTheme.typography.bodyMedium)
            Button(modifier = Modifier.fillMaxWidth(), onClick = onStart) {
                Text(ui.practiceLabel)
            }
        }
    }
}

private fun categoryLabel(category: WordCategory, languageCode: String?): String {
    val labels = when (languageCode) {
        "es" -> mapOf(
            WordCategory.PEOPLE to "Personas",
            WordCategory.FOOD to "Comida",
            WordCategory.PLACE to "Lugares",
            WordCategory.TIME to "Tiempo",
            WordCategory.ACTION to "Acciones",
            WordCategory.QUESTION to "Preguntas",
            WordCategory.HEALTH to "Salud",
        )
        "fr" -> mapOf(
            WordCategory.PEOPLE to "Personnes",
            WordCategory.FOOD to "Nourriture",
            WordCategory.PLACE to "Lieux",
            WordCategory.TIME to "Temps",
            WordCategory.ACTION to "Actions",
            WordCategory.QUESTION to "Questions",
            WordCategory.HEALTH to "Sant\u00e9",
        )
        "vi" -> mapOf(
            WordCategory.PEOPLE to "Con ng\u01b0\u1eddi",
            WordCategory.FOOD to "\u0110\u1ed3 \u0103n",
            WordCategory.PLACE to "\u0110\u1ecba \u0111i\u1ec3m",
            WordCategory.TIME to "Th\u1eddi gian",
            WordCategory.ACTION to "H\u00e0nh \u0111\u1ed9ng",
            WordCategory.QUESTION to "T\u1eeb h\u1ecfi",
            WordCategory.HEALTH to "S\u1ee9c kh\u1ecfe",
        )
        "th" -> mapOf(
            WordCategory.PEOPLE to "\u0e1c\u0e39\u0e49\u0e04\u0e19",
            WordCategory.FOOD to "\u0e2d\u0e32\u0e2b\u0e32\u0e23",
            WordCategory.PLACE to "\u0e2a\u0e16\u0e32\u0e19\u0e17\u0e35\u0e48",
            WordCategory.TIME to "\u0e40\u0e27\u0e25\u0e32",
            WordCategory.ACTION to "\u0e01\u0e32\u0e23\u0e01\u0e23\u0e30\u0e17\u0e33",
            WordCategory.QUESTION to "\u0e04\u0e33\u0e16\u0e32\u0e21",
            WordCategory.HEALTH to "\u0e2a\u0e38\u0e02\u0e20\u0e32\u0e1e",
        )
        "id" -> mapOf(
            WordCategory.PEOPLE to "Orang",
            WordCategory.FOOD to "Makanan",
            WordCategory.PLACE to "Tempat",
            WordCategory.TIME to "Waktu",
            WordCategory.ACTION to "Tindakan",
            WordCategory.QUESTION to "Pertanyaan",
            WordCategory.HEALTH to "Kesehatan",
        )
        else -> mapOf(
            WordCategory.PEOPLE to "People",
            WordCategory.FOOD to "Food",
            WordCategory.PLACE to "Places",
            WordCategory.TIME to "Time",
            WordCategory.ACTION to "Actions",
            WordCategory.QUESTION to "Question words",
            WordCategory.HEALTH to "Health",
        )
    }
    return labels.getValue(category)
}

private fun wordUiText(languageCode: String?): WordUiText = when (languageCode) {
    "es" -> WordUiText(
        title = "Palabras coreanas esenciales",
        intro = "Lee primero el coreano. Separa las s\u00edlabas, intenta recordar el significado y luego compru\u00e9balo.",
        showMeaning = "Ver significado",
        learned = "Ya la s\u00e9",
        learnedDone = "Aprendida",
        guidedTitle = "De palabras a conversacion",
        guidedIntro = "Usa las palabras nuevas en una frase real y luego practica la misma frase escuchando y hablando.",
        knownWordsLabel = "Palabras aprendidas",
        practiceLabel = "Practicar frase, escucha y habla",
        continueLabel = "Ir a frases",
    )
    "fr" -> WordUiText(
        title = "Mots cor\u00e9ens essentiels",
        intro = "Lis d'abord le cor\u00e9en. Observe les syllabes, devine le sens, puis v\u00e9rifie.",
        showMeaning = "Voir le sens",
        learned = "Je connais",
        learnedDone = "Appris",
        guidedTitle = "Des mots a la conversation",
        guidedIntro = "Utilise les nouveaux mots dans une vraie phrase, puis ecoute et dis cette meme phrase.",
        knownWordsLabel = "Mots appris",
        practiceLabel = "Phrase, ecoute et parole",
        continueLabel = "Passer aux phrases",
    )
    "vi" -> WordUiText(
        title = "T\u1eeb ti\u1ebfng H\u00e0n thi\u1ebft y\u1ebfu",
        intro = "\u0110\u1ecdc ti\u1ebfng H\u00e0n tr\u01b0\u1edbc. Nh\u00ecn c\u00e1ch t\u00e1ch \u00e2m ti\u1ebft, th\u1eed nh\u1edb ngh\u0129a r\u1ed3i m\u1edbi ki\u1ec3m tra.",
        showMeaning = "Xem ngh\u0129a",
        learned = "T\u00f4i \u0111\u00e3 nh\u1edb",
        learnedDone = "\u0110\u00e3 h\u1ecdc",
        guidedTitle = "T\u1eeb t\u1eeb v\u1ef1ng \u0111\u1ebfn h\u1ed9i tho\u1ea1i",
        guidedIntro = "D\u00f9ng t\u1eeb m\u1edbi trong m\u1ed9t c\u00e2u th\u1ef1c t\u1ebf, sau \u0111\u00f3 nghe v\u00e0 n\u00f3i ch\u00ednh c\u00e2u \u0111\u00f3.",
        knownWordsLabel = "T\u1eeb \u0111\u00e3 h\u1ecdc",
        practiceLabel = "Luy\u1ec7n c\u00e2u, nghe v\u00e0 n\u00f3i",
        continueLabel = "Sang c\u00e2u",
    )
    "th" -> WordUiText(
        title = "\u0e04\u0e33\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e17\u0e35\u0e48\u0e08\u0e33\u0e40\u0e1b\u0e47\u0e19",
        intro = "\u0e2d\u0e48\u0e32\u0e19\u0e20\u0e32\u0e29\u0e32\u0e40\u0e01\u0e32\u0e2b\u0e25\u0e35\u0e01\u0e48\u0e2d\u0e19 \u0e14\u0e39\u0e01\u0e32\u0e23\u0e41\u0e1a\u0e48\u0e07\u0e1e\u0e22\u0e32\u0e07\u0e04\u0e4c \u0e25\u0e2d\u0e07\u0e19\u0e36\u0e01\u0e04\u0e27\u0e32\u0e21\u0e2b\u0e21\u0e32\u0e22 \u0e41\u0e25\u0e49\u0e27\u0e04\u0e48\u0e2d\u0e22\u0e15\u0e23\u0e27\u0e08\u0e04\u0e33\u0e41\u0e1b\u0e25",
        showMeaning = "\u0e14\u0e39\u0e04\u0e27\u0e32\u0e21\u0e2b\u0e21\u0e32\u0e22",
        learned = "\u0e08\u0e33\u0e44\u0e14\u0e49\u0e41\u0e25\u0e49\u0e27",
        learnedDone = "\u0e40\u0e23\u0e35\u0e22\u0e19\u0e41\u0e25\u0e49\u0e27",
        guidedTitle = "\u0e08\u0e32\u0e01\u0e04\u0e33\u0e28\u0e31\u0e1e\u0e17\u0e4c\u0e2a\u0e39\u0e48\u0e1a\u0e17\u0e2a\u0e19\u0e17\u0e19\u0e32",
        guidedIntro = "\u0e43\u0e0a\u0e49\u0e04\u0e33\u0e43\u0e2b\u0e21\u0e48\u0e43\u0e19\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e08\u0e23\u0e34\u0e07 \u0e41\u0e25\u0e49\u0e27\u0e1f\u0e31\u0e07\u0e41\u0e25\u0e30\u0e1e\u0e39\u0e14\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04\u0e40\u0e14\u0e35\u0e22\u0e27\u0e01\u0e31\u0e19",
        knownWordsLabel = "\u0e04\u0e33\u0e17\u0e35\u0e48\u0e40\u0e23\u0e35\u0e22\u0e19\u0e41\u0e25\u0e49\u0e27",
        practiceLabel = "\u0e1d\u0e36\u0e01\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04 \u0e1f\u0e31\u0e07 \u0e41\u0e25\u0e30\u0e1e\u0e39\u0e14",
        continueLabel = "\u0e44\u0e1b\u0e40\u0e23\u0e35\u0e22\u0e19\u0e1b\u0e23\u0e30\u0e42\u0e22\u0e04",
    )
    "id" -> WordUiText(
        title = "Kata bahasa Korea penting",
        intro = "Baca bahasa Korea terlebih dahulu. Lihat pemisahan suku kata, tebak artinya, lalu periksa.",
        showMeaning = "Lihat arti",
        learned = "Saya sudah hafal",
        learnedDone = "Sudah dipelajari",
        guidedTitle = "Dari kata ke percakapan",
        guidedIntro = "Gunakan kata baru dalam kalimat nyata, lalu dengarkan dan ucapkan kalimat yang sama.",
        knownWordsLabel = "Kata dipelajari",
        practiceLabel = "Latih kalimat, dengar, dan bicara",
        continueLabel = "Lanjut ke kalimat",
    )
    else -> WordUiText(
        title = "Essential Korean words",
        intro = "Read the Korean first. Notice the syllable blocks, recall the meaning, then reveal it.",
        showMeaning = "Show meaning",
        learned = "I know this",
        learnedDone = "Learned",
        guidedTitle = "Words to real Korean",
        guidedIntro = "Use the new words in a real sentence, then listen to and say that same sentence.",
        knownWordsLabel = "Words learned",
        practiceLabel = "Practice sentence, listening and speaking",
        continueLabel = "Continue to sentences",
    )
}
