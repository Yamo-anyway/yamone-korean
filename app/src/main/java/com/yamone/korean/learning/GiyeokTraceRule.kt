package com.yamone.korean.learning

@Deprecated(
    message = "Use TraceEvaluator with a TraceLessonSpec.",
    replaceWith = ReplaceWith("TraceEvaluator.assess(listOf(points), BasicTraceCatalog.giyeok)"),
)
object GiyeokTraceRule {
    fun assess(points: List<NormalizedPoint>): TraceAssessment {
        return TraceEvaluator.assess(
            strokes = listOf(points),
            lesson = BasicTraceCatalog.giyeok,
        )
    }
}
