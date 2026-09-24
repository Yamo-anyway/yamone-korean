package com.yamone.korean.learning

enum class JamoType {
    CONSONANT,
    VOWEL,
}

data class JamoLesson(
    val id: String,
    val symbol: String,
    val type: JamoType,
    val soundGuide: String,
    val exampleSyllable: String,
    val order: Int,
)
