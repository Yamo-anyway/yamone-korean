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
    val packs: List<GuidedPracticePack> = emptyList()
}
