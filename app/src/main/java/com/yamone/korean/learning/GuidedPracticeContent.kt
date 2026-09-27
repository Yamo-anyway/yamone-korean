package com.yamone.korean.learning

data class GuidedPracticePack(
    val id: String,
    val sentenceId: String,
    val wordIds: List<String>,
    val titles: Map<String, String>,
) {
    fun title(languageCode: String?): String =
        titles[languageCode] ?: titles.getValue("en")
}

object GuidedPracticeCatalog {
    val packs: List<GuidedPracticePack> = listOf(
        GuidedPracticePack(
            id = "cafe",
            sentenceId = "sentence_order_one",
            wordIds = listOf("word_bibimbap", "word_juseyo"),
            titles = mapOf(
                "en" to "Cafe practice",
                "es" to "Pr\u00e1ctica de cafeter\u00eda",
                "fr" to "Pratique au caf\u00e9",
                "vi" to "Luy\u1ec7n t\u1eadp \u1edf qu\u00e1n c\u00e0 ph\u00ea",
                "th" to "\u0e1d\u0e36\u0e01\u0e17\u0e35\u0e48\u0e04\u0e32\u0e40\u0e1f\u0e48",
                "id" to "Latihan di kafe",
            ),
        ),
        GuidedPracticePack(
            id = "price",
            sentenceId = "sentence_price_question",
            wordIds = listOf("word_eolma"),
            titles = mapOf(
                "en" to "Price practice",
                "es" to "Pr\u00e1ctica de precios",
                "fr" to "Pratique des prix",
                "vi" to "Luy\u1ec7n h\u1ecfi gi\u00e1",
                "th" to "\u0e1d\u0e36\u0e01\u0e16\u0e32\u0e21\u0e23\u0e32\u0e04\u0e32",
                "id" to "Latihan harga",
            ),
        ),
        GuidedPracticePack(
            id = "place",
            sentenceId = "sentence_restroom_where",
            wordIds = listOf("word_hwajangsil", "word_eodi"),
            titles = mapOf(
                "en" to "Place practice",
                "es" to "Pr\u00e1ctica de lugares",
                "fr" to "Pratique des lieux",
                "vi" to "Luy\u1ec7n h\u1ecfi \u0111\u1ecba \u0111i\u1ec3m",
                "th" to "\u0e1d\u0e36\u0e01\u0e16\u0e32\u0e21\u0e2a\u0e16\u0e32\u0e19\u0e17\u0e35\u0e48",
                "id" to "Latihan tempat",
            ),
        ),
        GuidedPracticePack(
            id = "travel",
            sentenceId = "sentence_how_to_station",
            wordIds = listOf("word_seoulyeok", "word_eotteoke", "word_gayo"),
            titles = mapOf(
                "en" to "Travel practice",
                "es" to "Pr\u00e1ctica de viaje",
                "fr" to "Pratique du trajet",
                "vi" to "Luy\u1ec7n h\u1ecfi \u0111\u01b0\u1eddng",
                "th" to "\u0e1d\u0e36\u0e01\u0e16\u0e32\u0e21\u0e17\u0e32\u0e07",
                "id" to "Latihan perjalanan",
            ),
        ),
        GuidedPracticePack(
            id = "health",
            sentenceId = "sentence_head_hurts",
            wordIds = listOf("word_meori", "word_apayo"),
            titles = mapOf(
                "en" to "Health practice",
                "es" to "Pr\u00e1ctica de salud",
                "fr" to "Pratique sant\u00e9",
                "vi" to "Luy\u1ec7n n\u00f3i v\u1ec1 s\u1ee9c kh\u1ecfe",
                "th" to "\u0e1d\u0e36\u0e01\u0e1a\u0e2d\u0e01\u0e2d\u0e32\u0e01\u0e32\u0e23",
                "id" to "Latihan kesehatan",
            ),
        ),
        GuidedPracticePack(
            id = "repeat",
            sentenceId = "sentence_repeat_please",
            wordIds = listOf("word_dasi", "word_malhaeyo", "word_juseyo"),
            titles = mapOf(
                "en" to "Repeat practice",
                "es" to "Pr\u00e1ctica para repetir",
                "fr" to "Pratique de r\u00e9p\u00e9tition",
                "vi" to "Luy\u1ec7n nh\u1edd n\u00f3i l\u1ea1i",
                "th" to "\u0e1d\u0e36\u0e01\u0e02\u0e2d\u0e43\u0e2b\u0e49\u0e1e\u0e39\u0e14\u0e2d\u0e35\u0e01\u0e04\u0e23\u0e31\u0e49\u0e07",
                "id" to "Latihan mengulang",
            ),
        ),
        GuidedPracticePack(
            id = "plan",
            sentenceId = "sentence_meet_time",
            wordIds = listOf("word_myeot_si", "word_mannayo"),
            titles = mapOf(
                "en" to "Plan practice",
                "es" to "Pr\u00e1ctica de planes",
                "fr" to "Pratique des rendez-vous",
                "vi" to "Luy\u1ec7n h\u1eb9n g\u1eb7p",
                "th" to "\u0e1d\u0e36\u0e01\u0e19\u0e31\u0e14\u0e2b\u0e21\u0e32\u0e22",
                "id" to "Latihan membuat janji",
            ),
        ),
    )
}
