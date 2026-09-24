package com.yamone.korean.learning

import kotlin.math.sqrt

object TraceEvaluator {
    fun assess(
        strokes: List<List<NormalizedPoint>>,
        lesson: TraceLessonSpec,
    ): TraceAssessment {
        if (strokes.size != lesson.strokes.size) {
            return TraceAssessment(
                passed = false,
                reason = "stroke_count",
            )
        }

        lesson.strokes.forEachIndexed { strokeIndex, spec ->
            val points = strokes[strokeIndex]
            if (points.size < spec.minPointCount) {
                return TraceAssessment(false, "too_short", strokeIndex)
            }

            val guide = spec.guidePath
            if (guide.size < 2) {
                return TraceAssessment(false, "invalid_guide", strokeIndex)
            }

            if (distance(points.first(), guide.first()) > spec.startTolerance) {
                return TraceAssessment(false, "start_position", strokeIndex)
            }

            var searchFrom = 0
            spec.checkpoints.forEach { checkpoint ->
                val foundIndex = (searchFrom until points.size).firstOrNull { pointIndex ->
                    distance(points[pointIndex], checkpoint) <= spec.checkpointTolerance
                } ?: return TraceAssessment(false, "checkpoint_order", strokeIndex)
                searchFrom = foundIndex + 1
            }

            if (distance(points.last(), guide.last()) > spec.endTolerance) {
                return TraceAssessment(false, "end_position", strokeIndex)
            }

            val offGuideCount = points.count { point ->
                distanceToPolyline(point, guide) > spec.guideTolerance
            }
            val offGuideRatio = offGuideCount.toFloat() / points.size.toFloat()
            if (offGuideRatio > spec.maxOffGuideRatio) {
                return TraceAssessment(false, "guide_deviation", strokeIndex)
            }
        }

        return TraceAssessment(passed = true)
    }

    private fun distance(a: NormalizedPoint, b: NormalizedPoint): Float {
        val dx = a.x - b.x
        val dy = a.y - b.y
        return sqrt(dx * dx + dy * dy)
    }

    private fun distanceToPolyline(
        point: NormalizedPoint,
        path: List<NormalizedPoint>,
    ): Float {
        var best = Float.MAX_VALUE
        for (index in 1 until path.size) {
            best = minOf(
                best,
                distanceToSegment(
                    point = point,
                    start = path[index - 1],
                    end = path[index],
                ),
            )
        }
        return best
    }

    private fun distanceToSegment(
        point: NormalizedPoint,
        start: NormalizedPoint,
        end: NormalizedPoint,
    ): Float {
        val segmentX = end.x - start.x
        val segmentY = end.y - start.y
        val lengthSquared = segmentX * segmentX + segmentY * segmentY
        if (lengthSquared == 0f) return distance(point, start)

        val projection = (
            (point.x - start.x) * segmentX +
                (point.y - start.y) * segmentY
            ) / lengthSquared
        val t = projection.coerceIn(0f, 1f)
        val projected = NormalizedPoint(
            x = start.x + segmentX * t,
            y = start.y + segmentY * t,
        )
        return distance(point, projected)
    }
}
