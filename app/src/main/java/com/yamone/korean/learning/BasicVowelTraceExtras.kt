package com.yamone.korean.learning

object BasicVowelTraceExtras {
    val eu = TraceLessonSpec(
        id = "jamo_eu",
        symbol = "ㅡ",
        instruction = "Draw the long horizontal line from left to right.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.20f, 0.50f),
                    NormalizedPoint(0.80f, 0.50f),
                ),
            ),
        ),
    )

    val i = TraceLessonSpec(
        id = "jamo_i",
        symbol = "ㅣ",
        instruction = "Draw the long vertical line from top to bottom.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.18f),
                    NormalizedPoint(0.50f, 0.82f),
                ),
            ),
        ),
    )

    val lessons = listOf(eu, i)
}
