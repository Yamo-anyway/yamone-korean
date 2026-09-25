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
        BasicSyllableLesson("syllable_go", "jamo_giyeok", "ㄱ", "jamo_o", "ㅗ", "고", "go", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_gu", "jamo_giyeok", "ㄱ", "jamo_u", "ㅜ", "구", "gu", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_gi", "jamo_giyeok", "ㄱ", "jamo_i", "ㅣ", "기", "gi", SyllableVowelPlacement.RIGHT),

        BasicSyllableLesson("syllable_na", "jamo_nieun", "ㄴ", "jamo_a", "ㅏ", "나", "na", SyllableVowelPlacement.RIGHT),
        BasicSyllableLesson("syllable_no", "jamo_nieun", "ㄴ", "jamo_o", "ㅗ", "노", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_nu", "jamo_nieun", "ㄴ", "jamo_u", "ㅜ", "누", "nu", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_ni", "jamo_nieun", "ㄴ", "jamo_i", "ㅣ", "니", "ni", SyllableVowelPlacement.RIGHT),

        BasicSyllableLesson("syllable_ma", "jamo_mieum", "ㅁ", "jamo_a", "ㅏ", "마", "ma", SyllableVowelPlacement.RIGHT),
        BasicSyllableLesson("syllable_mo", "jamo_mieum", "ㅁ", "jamo_o", "ㅗ", "모", "mo", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_mu", "jamo_mieum", "ㅁ", "jamo_u", "ㅜ", "무", "mu", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_mi", "jamo_mieum", "ㅁ", "jamo_i", "ㅣ", "미", "mi", SyllableVowelPlacement.RIGHT),

        BasicSyllableLesson("syllable_ba", "jamo_bieup", "ㅂ", "jamo_a", "ㅏ", "바", "ba", SyllableVowelPlacement.RIGHT),
        BasicSyllableLesson("syllable_bo", "jamo_bieup", "ㅂ", "jamo_o", "ㅗ", "보", "bo", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_bu", "jamo_bieup", "ㅂ", "jamo_u", "ㅜ", "부", "bu", SyllableVowelPlacement.BELOW),
        BasicSyllableLesson("syllable_bi", "jamo_bieup", "ㅂ", "jamo_i", "ㅣ", "비", "bi", SyllableVowelPlacement.RIGHT),
    )

    fun byId(id: String): BasicSyllableLesson? = lessons.firstOrNull { it.id == id }
}

// Open-syllable composition is implemented by the interactive builder.
