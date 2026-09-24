package com.yamone.korean.learning

data class NormalizedPoint(
    val x: Float,
    val y: Float,
)

data class TraceStrokeSpec(
    val guidePath: List<NormalizedPoint>,
    val checkpoints: List<NormalizedPoint> = emptyList(),
    val startTolerance: Float = 0.16f,
    val checkpointTolerance: Float = 0.16f,
    val endTolerance: Float = 0.16f,
    val guideTolerance: Float = 0.15f,
    val maxOffGuideRatio: Float = 0.34f,
    val minPointCount: Int = 8,
)

data class TraceLessonSpec(
    val id: String,
    val symbol: String,
    val instruction: String,
    val strokes: List<TraceStrokeSpec>,
)

data class TraceAssessment(
    val passed: Boolean,
    val reason: String = "ok",
    val strokeIndex: Int? = null,
)
