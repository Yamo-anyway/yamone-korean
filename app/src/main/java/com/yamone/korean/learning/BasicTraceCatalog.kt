package com.yamone.korean.learning

object BasicTraceCatalog {
    val giyeok = TraceLessonSpec(
        id = "jamo_giyeok",
        symbol = "\u3131",
        instruction = "Start at the dot, move right, then turn and move down.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.24f, 0.28f),
                    NormalizedPoint(0.72f, 0.28f),
                    NormalizedPoint(0.72f, 0.74f),
                ),
                checkpoints = listOf(
                    NormalizedPoint(0.72f, 0.28f),
                ),
            ),
        ),
    )

    private val lessons = listOf(giyeok)

    fun byId(id: String): TraceLessonSpec? = lessons.firstOrNull { it.id == id }
}
