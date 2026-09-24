package com.yamone.korean.learning

object BasicTraceCatalog {
    val giyeok = TraceLessonSpec(
        id = "jamo_giyeok",
        symbol = "ㄱ",
        instruction = "Start at the dot, move right, then turn and move down.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.24f, 0.28f),
                    NormalizedPoint(0.72f, 0.28f),
                    NormalizedPoint(0.72f, 0.74f),
                ),
                checkpoints = listOf(NormalizedPoint(0.72f, 0.28f)),
            ),
        ),
    )

    val nieun = TraceLessonSpec(
        id = "jamo_nieun",
        symbol = "ㄴ",
        instruction = "Start at the top, move down, then turn and move right.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.30f, 0.24f),
                    NormalizedPoint(0.30f, 0.74f),
                    NormalizedPoint(0.72f, 0.74f),
                ),
                checkpoints = listOf(NormalizedPoint(0.30f, 0.74f)),
            ),
        ),
    )

    val digeut = TraceLessonSpec(
        id = "jamo_digeut",
        symbol = "ㄷ",
        instruction = "Draw the top, then the left side, then the bottom from left to right.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.26f),
                    NormalizedPoint(0.72f, 0.26f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.26f),
                    NormalizedPoint(0.28f, 0.74f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.74f),
                    NormalizedPoint(0.72f, 0.74f),
                ),
            ),
        ),
    )

    val a = TraceLessonSpec(
        id = "jamo_a",
        symbol = "ㅏ",
        instruction = "Draw the long vertical line down, then add the short line to the right.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.46f, 0.20f),
                    NormalizedPoint(0.46f, 0.80f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.46f, 0.48f),
                    NormalizedPoint(0.76f, 0.48f),
                ),
            ),
        ),
    )

    val orderedLessons = listOf(giyeok, nieun, digeut, a)

    fun byId(id: String): TraceLessonSpec? = orderedLessons.firstOrNull { it.id == id }
}
