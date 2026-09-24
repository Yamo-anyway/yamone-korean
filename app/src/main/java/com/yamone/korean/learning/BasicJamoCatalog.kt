package com.yamone.korean.learning

/**
 * Offline starter curriculum for the 24 basic Hangul jamo.
 *
 * Stable lesson IDs are used by local progress storage and should remain
 * unchanged after release.
 */
object BasicJamoCatalog {
    val lessons: List<JamoLesson> = listOf(
        JamoLesson("jamo_giyeok", "ㄱ", JamoType.CONSONANT, "g / k", "가", 1),
        JamoLesson("jamo_nieun", "ㄴ", JamoType.CONSONANT, "n", "나", 2),
        JamoLesson("jamo_digeut", "ㄷ", JamoType.CONSONANT, "d / t", "다", 3),
        JamoLesson("jamo_rieul", "ㄹ", JamoType.CONSONANT, "r / l", "라", 4),
        JamoLesson("jamo_mieum", "ㅁ", JamoType.CONSONANT, "m", "마", 5),
        JamoLesson("jamo_bieup", "ㅂ", JamoType.CONSONANT, "b / p", "바", 6),
        JamoLesson("jamo_siot", "ㅅ", JamoType.CONSONANT, "s", "사", 7),
        JamoLesson("jamo_ieung", "ㅇ", JamoType.CONSONANT, "silent / ng", "아", 8),
        JamoLesson("jamo_jieut", "ㅈ", JamoType.CONSONANT, "j", "자", 9),
        JamoLesson("jamo_chieut", "ㅊ", JamoType.CONSONANT, "ch", "차", 10),
        JamoLesson("jamo_kieuk", "ㅋ", JamoType.CONSONANT, "k", "카", 11),
        JamoLesson("jamo_tieut", "ㅌ", JamoType.CONSONANT, "t", "타", 12),
        JamoLesson("jamo_pieup", "ㅍ", JamoType.CONSONANT, "p", "파", 13),
        JamoLesson("jamo_hieut", "ㅎ", JamoType.CONSONANT, "h", "하", 14),
        JamoLesson("jamo_a", "ㅏ", JamoType.VOWEL, "a", "아", 15),
        JamoLesson("jamo_ya", "ㅑ", JamoType.VOWEL, "ya", "야", 16),
        JamoLesson("jamo_eo", "ㅓ", JamoType.VOWEL, "eo", "어", 17),
        JamoLesson("jamo_yeo", "ㅕ", JamoType.VOWEL, "yeo", "여", 18),
        JamoLesson("jamo_o", "ㅗ", JamoType.VOWEL, "o", "오", 19),
        JamoLesson("jamo_yo", "ㅛ", JamoType.VOWEL, "yo", "요", 20),
        JamoLesson("jamo_u", "ㅜ", JamoType.VOWEL, "u", "우", 21),
        JamoLesson("jamo_yu", "ㅠ", JamoType.VOWEL, "yu", "유", 22),
        JamoLesson("jamo_eu", "ㅡ", JamoType.VOWEL, "eu", "으", 23),
        JamoLesson("jamo_i", "ㅣ", JamoType.VOWEL, "i", "이", 24),
    )

    val consonants: List<JamoLesson> = lessons.filter { it.type == JamoType.CONSONANT }
    val vowels: List<JamoLesson> = lessons.filter { it.type == JamoType.VOWEL }

    fun findById(id: String): JamoLesson? = lessons.firstOrNull { it.id == id }
}
