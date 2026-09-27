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
            wordIds = listOf("word_bibimbap"),
            titles = mapOf("en" to "Cafe practice"),
        ),
        GuidedPracticePack(
            id = "price",
            sentenceId = "sentence_price_question",
            wordIds = listOf("word_eolma"),
            titles = mapOf("en" to "Price practice"),
        ),
    )
}
