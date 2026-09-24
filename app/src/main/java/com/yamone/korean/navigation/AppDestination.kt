package com.yamone.korean.navigation

enum class AppDestination(
    val route: String,
    val title: String,
) {
    Language("language", "Choose language"),
    Home("home", "Yamone Korean"),
    Jamo("jamo", "Hangul letters"),
    Trace("trace", "Touch writing"),
    Write("write", "Free writing"),
    Syllable("syllable", "Syllables"),
    Word("word", "Words"),
    Sentence("sentence", "Sentences"),
    Listening("listening", "Listening"),
    Speaking("speaking", "Speaking"),
    Review("review", "Review"),
    Settings("settings", "Settings"),
}

val learningDestinations = listOf(
    AppDestination.Jamo,
    AppDestination.Trace,
    AppDestination.Write,
    AppDestination.Syllable,
    AppDestination.Word,
    AppDestination.Sentence,
    AppDestination.Listening,
    AppDestination.Speaking,
    AppDestination.Review,
)
