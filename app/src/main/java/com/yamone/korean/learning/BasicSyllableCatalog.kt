package com.yamone.korean.learning

enum class SyllableVowelPlacement {
    RIGHT,
    BELOW,
}

data class BasicSyllableLesson(
    val id: String,
    val initialJamoId: String,
    val initial: String,
    val vowelJamoId: String,
    val vowel: String,
    val syllable: String,
    val soundGuide: String,
    val vowelPlacement: SyllableVowelPlacement,
)

object BasicSyllableCatalog {
    val lessons: List<BasicSyllableLesson> = listOf(
        BasicSyllableLesson("syllable_ga", "jamo_giyeok", "ㄱ", "jamo_a", "ㅏ", "가", "ga", SyllableVowelPlacement.RIGHT),
        BasicSyllableLesson("syllable_na", "jamo_nieun", "ㄴ", "jamo_a", "ㅏ", "나", "na", SyllableVowelPlacement.RIGHT),
        BasicSyllableLesson("syllable_mi", "jamo_mieum", "ㅁ", "jamo_i", "ㅣ", "미", "mi", SyllableVowelPlacement.RIGHT),
        BasicSyllableLesson("syllable_go", "jamo_giyeok", "ㄱ", "jamo_o", "ㅗ", "고", "go", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_nu", "jamo_nieun", "ㄴ", "jamo_u", "ㅜ", "누", "nu", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_bu", "jamo_bieup", "ㅂ", "jamo_u", "ㅜ", "부", "bu", SyllableVowelPlacement.BELOW),
    )

    fun byId(id: String): BasicSyllableLesson? = lessons.firstOrNull { it.id == id }
}
