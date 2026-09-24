package com.yamone.korean.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.yamone.korean.learning.BasicTraceCatalog
import com.yamone.korean.learning.NormalizedPoint
import com.yamone.korean.learning.TraceEvaluator

@Composable
fun TraceScreen(onContinue: () -> Unit) {
    val lessons = remember { BasicTraceCatalog.orderedLessons }
    var lessonIndex by remember { mutableStateOf(0) }
    val lesson = lessons[lessonIndex]
    val completedStrokes = remember(lesson.id) { mutableStateListOf<List<Offset>>() }
    var activeStroke by remember(lesson.id) { mutableStateOf<List<Offset>>(emptyList()) }
    var passed by remember(lesson.id) { mutableStateOf(false) }
    var feedback by remember(lesson.id) { mutableStateOf<String?>(null) }
    val guideColor = MaterialTheme.colorScheme.outlineVariant
    val writingColor = MaterialTheme.colorScheme.primary
    val isLastLesson = lessonIndex == lessons.lastIndex

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Touch writing · " + lesson.symbol, style = MaterialTheme.typography.headlineMedium)
        Text(
            "Letter " + (lessonIndex + 1) + " of " + lessons.size,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(lesson.instruction, style = MaterialTheme.typography.bodyLarge)

        feedback?.let {
            Text(
                it,
                color = if (passed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Canvas(
            modifier = Modifier.fillMaxWidth().weight(1f).aspectRatio(1f).pointerInput(lesson.id) {
                detectDragGestures(
                    onDragStart = {
                        if (completedStrokes.size >= lesson.strokes.size) completedStrokes.clear()
                        activeStroke = listOf(it)
                        passed = false
                        feedback = null
                    },
                    onDragEnd = {
                        val finishedStroke = activeStroke
                        activeStroke = emptyList()
                        if (finishedStroke.size > 1) completedStrokes.add(finishedStroke)

                        if (completedStrokes.size == lesson.strokes.size) {
                            val normalized = completedStrokes.map { stroke ->
                                stroke.map { point ->
                                    NormalizedPoint(
                                        x = point.x / size.width.toFloat(),
                                        y = point.y / size.height.toFloat(),
                                    )
                                }
                            }
                            val result = TraceEvaluator.assess(normalized, lesson)
                            passed = result.passed
                            feedback = if (result.passed) {
                                "Great! " + lesson.symbol + " was traced correctly."
                            } else {
                                traceFeedback(result.reason, result.strokeIndex)
                            }
                        } else if (completedStrokes.isNotEmpty()) {
                            feedback = "Stroke " + completedStrokes.size + " of " + lesson.strokes.size + " complete."
                        }
                    },
                    onDragCancel = { activeStroke = emptyList() },
                    onDrag = { change, _ -> activeStroke = activeStroke + change.position },
                )
            },
        ) {
            lesson.strokes.forEach { stroke ->
                val guidePoints = stroke.guidePath.map { point ->
                    Offset(point.x * size.width, point.y * size.height)
                }
                if (guidePoints.size >= 2) {
                    val path = Path().apply {
                        moveTo(guidePoints.first().x, guidePoints.first().y)
                        guidePoints.drop(1).forEach { point -> lineTo(point.x, point.y) }
                    }
                    drawPath(
                        path,
                        guideColor,
                        style = Stroke(size.minDimension * 0.055f, cap = StrokeCap.Round),
                    )
                }
                guidePoints.firstOrNull()?.let { start ->
                    drawCircle(writingColor, size.minDimension * 0.025f, start)
                }
            }

            fun render(points: List<Offset>) {
                for (index in 1 until points.size) {
                    drawLine(
                        writingColor,
                        points[index - 1],
                        points[index],
                        size.minDimension * 0.035f,
                        cap = StrokeCap.Round,
                    )
                }
            }
            completedStrokes.forEach(::render)
            render(activeStroke)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    completedStrokes.clear()
                    activeStroke = emptyList()
                    passed = false
                    feedback = null
                },
            ) { Text("Clear") }

            Button(
                modifier = Modifier.weight(1f),
                enabled = passed,
                onClick = {
                    if (isLastLesson) onContinue() else lessonIndex += 1
                },
            ) { Text(if (isLastLesson) "Continue" else "Next letter") }
        }
    }
}

private fun traceFeedback(reason: String, strokeIndex: Int?): String {
    val prefix = strokeIndex?.let { "Stroke " + (it + 1) + ": " }.orEmpty()
    val message = when (reason) {
        "stroke_count" -> "Follow the displayed stroke count."
        "too_short" -> "Complete the full path."
        "start_position" -> "Start closer to the dot."
        "checkpoint_order" -> "Follow the guide in order."
        "guide_deviation" -> "Stay closer to the guide line."
        "end_position" -> "Finish near the end of the guide."
        "invalid_guide" -> "This lesson guide is not ready yet."
        else -> "Try again."
    }
    return prefix + message
}
