package com.yamone.korean.learning

data class NormalizedPoint(val x: Float, val y: Float)

data class GiyeokTraceAssessment(
    val passed: Boolean,
    val reason: String,
)

object GiyeokTraceRule {
    fun assess(points: List<NormalizedPoint>): GiyeokTraceAssessment {
        if (points.size < 8) return GiyeokTraceAssessment(false, "too_short")

        val start = points.first()
        val end = points.last()
        if (start.x !in 0.10f..0.38f || start.y !in 0.14f..0.42f) {
            return GiyeokTraceAssessment(false, "start_position")
        }

        val cornerIndex = points.indices.minByOrNull { index ->
            val point = points[index]
            val dx = point.x - 0.72f
            val dy = point.y - 0.28f
            dx * dx + dy * dy
        } ?: return GiyeokTraceAssessment(false, "corner_position")

        if (cornerIndex < 2 || cornerIndex > points.lastIndex - 2) {
            return GiyeokTraceAssessment(false, "corner_position")
        }

        val corner = points[cornerIndex]
        if (corner.x - start.x < 0.30f) {
            return GiyeokTraceAssessment(false, "move_right_first")
        }
        if (end.y - corner.y < 0.28f) {
            return GiyeokTraceAssessment(false, "move_down_second")
        }
        if (kotlin.math.abs(end.x - corner.x) > 0.18f) {
            return GiyeokTraceAssessment(false, "vertical_alignment")
        }
        if (end.x !in 0.54f..0.90f || end.y !in 0.56f..0.92f) {
            return GiyeokTraceAssessment(false, "end_position")
        }

        return GiyeokTraceAssessment(true, "ok")
    }
}
