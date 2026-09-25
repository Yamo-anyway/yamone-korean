package com.yamone.korean.learning

object BasicJamoCatalog {
    val lessons = listOf(
        JamoLesson("jamo.consonant.01.giyeok", "ㄱ", JamoType.CONSONANT, "g / k", "가", 1),
        JamoLesson("jamo.consonant.02.nieun", "ㄴ", JamoType.CONSONANT, "n", "나", 2),
        JamoLesson("jamo.consonant.03.digeut", "ㄷ", JamoType.CONSONANT, "d / t", "다", 3),
        JamoLesson("jamo.consonant.04.rieul", "ㄹ", JamoType.CONSONANT, "r / l", "라", 4),
        JamoLesson("jamo.consonant.05.mieum", "ㅁ", JamoType.CONSONANT, "m", "마", 5),
        JamoLesson("jamo.consonant.06.bieup", "ㅂ", JamoType.CONSONANT, "b / p", "바", 6),
        JamoLesson("jamo.consonant.07.siot", "ㅅ", JamoType.CONSONANT, "s", "사", 7),
        JamoLesson("jamo.consonant.08.ieung", "ㅇ", JamoType.CONSONANT, "silent / ng", "아", 8),
        JamoLesson("jamo.consonant.09.jieut", "ㅈ", JamoType.CONSONANT, "j", "자", 9),
        JamoLesson("jamo.consonant.10.chieut", "ㅊ", JamoType.CONSONANT, "ch", "차", 10),
        JamoLesson("jamo.consonant.11.kieuk", "ㅋ", JamoType.CONSONANT, "k", "카", 11),
        JamoLesson("jamo.consonant.12.tieut", "ㅌ", JamoType.CONSONANT, "t", "타", 12),
        JamoLesson("jamo.consonant.13.pieup", "ㅍ", JamoType.CONSONANT, "p", "파", 13),
        JamoLesson("jamo.consonant.14.hieut", "ㅎ", JamoType.CONSONANT, "h", "하", 14),
        JamoLesson("jamo.vowel.01.a", "ㅏ", JamoType.VOWEL, "a", "아", 15),
        JamoLesson("jamo.vowel.02.ya", "ㅑ", JamoType.VOWEL, "ya", "야", 16),
        JamoLesson("jamo.vowel.03.eo", "ㅓ", JamoType.VOWEL, "eo", "어", 17),
        JamoLesson("jamo.vowel.04.yeo", "ㅕ", JamoType.VOWEL, "yeo", "여", 18),
        JamoLesson("jamo.vowel.05.o", "ㅗ", JamoType.VOWEL, "o", "오", 19),
        JamoLesson("jamo.vowel.06.yo", "ㅛ", JamoType.VOWEL, "yo", "요", 20),
        JamoLesson("jamo.vowel.07.u", "ㅜ", JamoType.VOWEL, "u", "우", 21),
        JamoLesson("jamo.vowel.08.yu", "ㅠ", JamoType.VOWEL, "yu", "유", 22),
        JamoLesson("jamo.vowel.09.eu", "ㅡ", JamoType.VOWEL, "eu", "으", 23),
        JamoLesson("jamo.vowel.10.i", "ㅣ", JamoType.VOWEL, "i", "이", 24),
    )

    val consonants = lessons.filter { it.type == JamoType.CONSONANT }
    val vowels = lessons.filter { it.type == JamoType.VOWEL }

    fun findById(id: String): JamoLesson? = lessons.firstOrNull { it.id == id }
}
