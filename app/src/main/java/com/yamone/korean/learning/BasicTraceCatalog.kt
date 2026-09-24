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
                checkpoints = listOf(NormalizedPoint(0.72f, 0.28f)),
            ),
        ),
    )

    val nieun = TraceLessonSpec(
        id = "jamo_nieun",
        symbol = "\u3134",
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
        symbol = "\u3137",
        instruction = "Draw the top line first, then draw the left side down and turn right along the bottom.",
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
                    NormalizedPoint(0.72f, 0.74f),
                ),
                checkpoints = listOf(NormalizedPoint(0.28f, 0.74f)),
            ),
        ),
    )

    val rieul = TraceLessonSpec(
        id = "jamo_rieul",
        symbol = "\u3139",
        instruction = "Draw a top-right corner, move left across the middle, then draw down and right to finish.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.22f),
                    NormalizedPoint(0.72f, 0.22f),
                    NormalizedPoint(0.72f, 0.44f),
                ),
                checkpoints = listOf(NormalizedPoint(0.72f, 0.22f)),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.72f, 0.44f),
                    NormalizedPoint(0.32f, 0.44f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.32f, 0.44f),
                    NormalizedPoint(0.32f, 0.74f),
                    NormalizedPoint(0.72f, 0.74f),
                ),
                checkpoints = listOf(NormalizedPoint(0.32f, 0.74f)),
            ),
        ),
    )

    val mieum = TraceLessonSpec(
        id = "jamo_mieum",
        symbol = "\u3141",
        instruction = "Draw the left side down, draw the top and right side in one stroke, then close the bottom.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.24f),
                    NormalizedPoint(0.28f, 0.76f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.24f),
                    NormalizedPoint(0.72f, 0.24f),
                    NormalizedPoint(0.72f, 0.76f),
                ),
                checkpoints = listOf(NormalizedPoint(0.72f, 0.24f)),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.76f),
                    NormalizedPoint(0.72f, 0.76f),
                ),
            ),
        ),
    )

    val bieup = TraceLessonSpec(
        id = "jamo_bieup",
        symbol = "\u3142",
        instruction = "Draw the left side, then the top and right side, then the middle line, and finally the bottom line.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.22f),
                    NormalizedPoint(0.28f, 0.78f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.22f),
                    NormalizedPoint(0.72f, 0.22f),
                    NormalizedPoint(0.72f, 0.78f),
                ),
                checkpoints = listOf(NormalizedPoint(0.72f, 0.22f)),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.50f),
                    NormalizedPoint(0.72f, 0.50f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.78f),
                    NormalizedPoint(0.72f, 0.78f),
                ),
            ),
        ),
    )

    val siot = TraceLessonSpec(
        id = "jamo_siot",
        symbol = "\u3145",
        instruction = "Draw from the center down-left first, then start at the center again and draw down-right.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.22f),
                    NormalizedPoint(0.28f, 0.76f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.22f),
                    NormalizedPoint(0.72f, 0.76f),
                ),
            ),
        ),
    )

    val a = TraceLessonSpec(
        id = "jamo_a",
        symbol = "\u314F",
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

    val orderedLessons = listOf(
        giyeok,
        nieun,
        digeut,
        rieul,
        mieum,
        bieup,
        siot,
        a,
    )

    fun byId(id: String): TraceLessonSpec? = orderedLessons.firstOrNull { it.id == id }
}
