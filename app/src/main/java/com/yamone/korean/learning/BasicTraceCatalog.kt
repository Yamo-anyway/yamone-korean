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

    val ieung = TraceLessonSpec(
        id = "jamo_ieung",
        symbol = "\u3147",
        instruction = "Start at the top and draw one smooth circle back to the starting point.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.22f),
                    NormalizedPoint(0.34f, 0.27f),
                    NormalizedPoint(0.26f, 0.50f),
                    NormalizedPoint(0.34f, 0.73f),
                    NormalizedPoint(0.50f, 0.78f),
                    NormalizedPoint(0.66f, 0.73f),
                    NormalizedPoint(0.74f, 0.50f),
                    NormalizedPoint(0.66f, 0.27f),
                    NormalizedPoint(0.50f, 0.22f),
                ),
                checkpoints = listOf(
                    NormalizedPoint(0.26f, 0.50f),
                    NormalizedPoint(0.50f, 0.78f),
                    NormalizedPoint(0.74f, 0.50f),
                ),
            ),
        ),
    )

    val jieut = TraceLessonSpec(
        id = "jamo_jieut",
        symbol = "\u3148",
        instruction = "Draw the top line and continue diagonally down-left, then draw the right diagonal.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.30f, 0.26f),
                    NormalizedPoint(0.70f, 0.26f),
                    NormalizedPoint(0.30f, 0.76f),
                ),
                checkpoints = listOf(NormalizedPoint(0.70f, 0.26f)),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.34f),
                    NormalizedPoint(0.70f, 0.76f),
                ),
            ),
        ),
    )

    val chieut = TraceLessonSpec(
        id = "jamo_chieut",
        symbol = "\u314A",
        instruction = "Draw the short top line, draw the second line and continue diagonally down-left, then draw the right diagonal.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.40f, 0.18f),
                    NormalizedPoint(0.60f, 0.18f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.30f, 0.34f),
                    NormalizedPoint(0.70f, 0.34f),
                    NormalizedPoint(0.30f, 0.78f),
                ),
                checkpoints = listOf(NormalizedPoint(0.70f, 0.34f)),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.40f),
                    NormalizedPoint(0.70f, 0.78f),
                ),
            ),
        ),
    )

    val kieuk = TraceLessonSpec(
        id = "jamo_kieuk",
        symbol = "\u314B",
        instruction = "Draw the giyeok shape first, then add the middle horizontal line.",
        strokes = listOf(
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
                    NormalizedPoint(0.30f, 0.50f),
                    NormalizedPoint(0.70f, 0.50f),
                ),
            ),
        ),
    )

    val tieut = TraceLessonSpec(
        id = "jamo_tieut",
        symbol = "ㅌ",
        instruction = "Draw the top horizontal line, then the middle horizontal line, and finish with the nieun-shaped stroke.",
        strokes = listOf(
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.28f, 0.22f), NormalizedPoint(0.72f, 0.22f))),
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.30f, 0.46f), NormalizedPoint(0.70f, 0.46f))),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.28f, 0.22f),
                    NormalizedPoint(0.28f, 0.78f),
                    NormalizedPoint(0.72f, 0.78f),
                ),
                checkpoints = listOf(NormalizedPoint(0.28f, 0.78f)),
            ),
        ),
    )

    val pieup = TraceLessonSpec(
        id = "jamo_pieup",
        symbol = "ㅍ",
        instruction = "Draw the top line, the left vertical, the right vertical, and then the bottom line.",
        strokes = listOf(
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.28f, 0.24f), NormalizedPoint(0.72f, 0.24f))),
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.28f, 0.24f), NormalizedPoint(0.28f, 0.76f))),
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.72f, 0.24f), NormalizedPoint(0.72f, 0.76f))),
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.28f, 0.76f), NormalizedPoint(0.72f, 0.76f))),
        ),
    )

    val hieuh = TraceLessonSpec(
        id = "jamo_hieuh",
        symbol = "ㅎ",
        instruction = "Draw the short top mark, then the horizontal line, and finish with the circle.",
        strokes = listOf(
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.50f, 0.14f), NormalizedPoint(0.50f, 0.28f))),
            TraceStrokeSpec(guidePath = listOf(NormalizedPoint(0.34f, 0.34f), NormalizedPoint(0.66f, 0.34f))),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.46f),
                    NormalizedPoint(0.36f, 0.50f),
                    NormalizedPoint(0.30f, 0.64f),
                    NormalizedPoint(0.36f, 0.78f),
                    NormalizedPoint(0.50f, 0.82f),
                    NormalizedPoint(0.64f, 0.78f),
                    NormalizedPoint(0.70f, 0.64f),
                    NormalizedPoint(0.64f, 0.50f),
                    NormalizedPoint(0.50f, 0.46f),
                ),
                checkpoints = listOf(
                    NormalizedPoint(0.30f, 0.64f),
                    NormalizedPoint(0.50f, 0.82f),
                    NormalizedPoint(0.70f, 0.64f),
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

    val ya = TraceLessonSpec(
        id = "jamo_ya",
        symbol = "ㅑ",
        instruction = "Draw the long vertical line down, then add the upper and lower short lines to the right.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.46f, 0.16f),
                    NormalizedPoint(0.46f, 0.84f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.46f, 0.38f),
                    NormalizedPoint(0.76f, 0.38f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.46f, 0.62f),
                    NormalizedPoint(0.76f, 0.62f),
                ),
            ),
        ),
    )

    val eo = TraceLessonSpec(
        id = "jamo_eo",
        symbol = "ㅓ",
        instruction = "Draw the short horizontal line from left to right, then draw the long vertical line down.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.24f, 0.48f),
                    NormalizedPoint(0.52f, 0.48f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.52f, 0.18f),
                    NormalizedPoint(0.52f, 0.82f),
                ),
            ),
        ),
    )

    val yeo = TraceLessonSpec(
        id = "jamo_yeo",
        symbol = "ㅕ",
        instruction = "Draw the upper and lower short lines from left to right, then draw the long vertical line down.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.22f, 0.38f),
                    NormalizedPoint(0.52f, 0.38f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.22f, 0.62f),
                    NormalizedPoint(0.52f, 0.62f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.52f, 0.16f),
                    NormalizedPoint(0.52f, 0.84f),
                ),
            ),
        ),
    )

    val o = TraceLessonSpec(
        id = "jamo_o",
        symbol = "ㅗ",
        instruction = "Draw the short vertical line downward, then draw the long horizontal line from left to right.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.18f),
                    NormalizedPoint(0.50f, 0.50f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.22f, 0.50f),
                    NormalizedPoint(0.78f, 0.50f),
                ),
            ),
        ),
    )

    val yo = TraceLessonSpec(
        id = "jamo_yo",
        symbol = "ㅛ",
        instruction = "Draw the left short vertical line downward, then the right one, and finish with the long horizontal line.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.42f, 0.18f),
                    NormalizedPoint(0.42f, 0.50f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.58f, 0.18f),
                    NormalizedPoint(0.58f, 0.50f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.20f, 0.50f),
                    NormalizedPoint(0.80f, 0.50f),
                ),
            ),
        ),
    )

    val u = TraceLessonSpec(
        id = "jamo_u",
        symbol = "ㅜ",
        instruction = "Draw the long horizontal line from left to right, then draw the short vertical line downward from the center.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.22f, 0.42f),
                    NormalizedPoint(0.78f, 0.42f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.50f, 0.42f),
                    NormalizedPoint(0.50f, 0.82f),
                ),
            ),
        ),
    )

    val yu = TraceLessonSpec(
        id = "jamo_yu",
        symbol = "ㅠ",
        instruction = "Draw the long horizontal line first, then draw the left and right short vertical lines downward.",
        strokes = listOf(
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.20f, 0.42f),
                    NormalizedPoint(0.80f, 0.42f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.42f, 0.42f),
                    NormalizedPoint(0.42f, 0.80f),
                ),
            ),
            TraceStrokeSpec(
                guidePath = listOf(
                    NormalizedPoint(0.58f, 0.42f),
                    NormalizedPoint(0.58f, 0.80f),
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
        ieung,
        jieut,
        chieut,
        kieuk,
        tieut,
        pieup,
        hieuh,
        a,
        ya,
        eo,
        yeo,
        o,
        yo,
        u,
        yu,
    )

    fun byId(id: String): TraceLessonSpec? = orderedLessons.firstOrNull { it.id == id }
}
