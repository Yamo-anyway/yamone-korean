package com.yamone.korean.learning

data class FinalConsonantReadingExample(
    val id: String,
    val finalConsonant: String,
    val syllable: String,
    val decomposition: String,
    val soundGuide: String,
    private val meanings: Map<String, String>,
) {
    fun meaning(languageCode: String?): String =
        meanings[languageCode] ?: meanings.getValue("en")
}

object FinalConsonantReadingCatalog {
    val examples: List<FinalConsonantReadingExample> = listOf(
        FinalConsonantReadingExample(
            id = "batchim_guk",
            finalConsonant = "ㄱ",
            syllable = "국",
            decomposition = "ㄱ + ㅜ + ㄱ",
            soundGuide = "guk",
            meanings = mapOf(
                "en" to "soup",
                "es" to "sopa",
                "fr" to "soupe",
                "vi" to "canh",
                "th" to "ซุป",
                "id" to "sup",
            ),
        ),
        FinalConsonantReadingExample(
            id = "batchim_san",
            finalConsonant = "ㄴ",
            syllable = "산",
            decomposition = "ㅅ + ㅏ + ㄴ",
            soundGuide = "san",
            meanings = mapOf(
                "en" to "mountain",
                "es" to "montaña",
                "fr" to "montagne",
                "vi" to "núi",
                "th" to "ภูเขา",
                "id" to "gunung",
            ),
        ),
        FinalConsonantReadingExample(
            id = "batchim_got",
            finalConsonant = "ㄷ",
            syllable = "곧",
            decomposition = "ㄱ + ㅗ + ㄷ",
            soundGuide = "got",
            meanings = mapOf(
                "en" to "soon",
                "es" to "pronto",
                "fr" to "bientôt",
                "vi" to "sớm",
                "th" to "เร็ว ๆ นี้",
                "id" to "segera",
            ),
        ),
        FinalConsonantReadingExample(
            id = "batchim_mul",
            finalConsonant = "ㄹ",
            syllable = "물",
            decomposition = "ㅁ + ㅜ + ㄹ",
            soundGuide = "mul",
            meanings = mapOf(
                "en" to "water",
                "es" to "agua",
                "fr" to "eau",
                "vi" to "nước",
                "th" to "น้ำ",
                "id" to "air",
            ),
        ),
        FinalConsonantReadingExample(
            id = "batchim_bam",
            finalConsonant = "ㅁ",
            syllable = "밤",
            decomposition = "ㅂ + ㅏ + ㅁ",
            soundGuide = "bam",
            meanings = mapOf(
                "en" to "night",
                "es" to "noche",
                "fr" to "nuit",
                "vi" to "đêm",
                "th" to "กลางคืน",
                "id" to "malam",
            ),
        ),
        FinalConsonantReadingExample(
            id = "batchim_bap",
            finalConsonant = "ㅂ",
            syllable = "밥",
            decomposition = "ㅂ + ㅏ + ㅂ",
            soundGuide = "bap",
            meanings = mapOf(
                "en" to "rice / meal",
                "es" to "arroz / comida",
                "fr" to "riz / repas",
                "vi" to "cơm / bữa ăn",
                "th" to "ข้าว / มื้ออาหาร",
                "id" to "nasi / makanan",
            ),
        ),
        FinalConsonantReadingExample(
            id = "batchim_bang",
            finalConsonant = "ㅇ",
            syllable = "방",
            decomposition = "ㅂ + ㅏ + ㅇ",
            soundGuide = "bang",
            meanings = mapOf(
                "en" to "room",
                "es" to "habitación",
                "fr" to "chambre",
                "vi" to "phòng",
                "th" to "ห้อง",
                "id" to "kamar",
            ),
        ),
    )
}
