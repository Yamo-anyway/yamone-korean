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
import com.yamone.korean.learning.GiyeokTraceRule
import com.yamone.korean.learning.NormalizedPoint

@Composable
fun TraceScreen(
    onContinue: () -> Unit,
) {
    val completedStrokes = remember { mutableStateListOf<List<Offset>>() }
    var activeStroke by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var passed by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf<String?>(null) }
    val guideColor = MaterialTheme.colorScheme.outlineVariant
    val writingColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Touch writing",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Trace ㄱ with your finger. Start at the dot, move right, then down.",
            style = MaterialTheme.typography.bodyLarge,
        )

        feedback?.let {
            Text(
                text = it,
                color = if (passed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1f)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            activeStroke = listOf(it)
                            passed = false
                            feedback = null
                        },
                        onDragEnd = {
                            if (activeStroke.size > 1) {
                                completedStrokes.add(activeStroke)
                                if (completedStrokes.size > 1) {
                                    passed = false
                                    feedback = "Use one continuous stroke for ㄱ."
                                } else {
                                    val points = activeStroke.map { point ->
                                        NormalizedPoint(
                                            x = point.x / size.width.toFloat(),
                                            y = point.y / size.height.toFloat(),
                                        )
                                    }
                                    val result = GiyeokTraceRule.assess(points)
                                    passed = result.passed
                                    feedback = if (result.passed) {
                                        "Great! ㄱ was traced correctly."
                                    } else {
                                        traceFeedback(result.reason)
                                    }
                                }
                            }
                            activeStroke = emptyList()
                        },
                        onDragCancel = {
                            activeStroke = emptyList()
                        },
                        onDrag = { change, _ ->
                            activeStroke = activeStroke + change.position
                        },
                    )
                },
        ) {
            val left = size.width * 0.24f
            val top = size.height * 0.28f
            val right = size.width * 0.72f
            val bottom = size.height * 0.74f
            val guidePath = Path().apply {
                moveTo(left, top)
                lineTo(right, top)
                lineTo(right, bottom)
            }

            drawPath(
                path = guidePath,
                color = guideColor,
                style = Stroke(
                    width = size.minDimension * 0.055f,
                    cap = StrokeCap.Round,
                ),
            )
            drawCircle(
                color = writingColor,
                radius = size.minDimension * 0.025f,
                center = Offset(left, top),
            )

            fun drawStroke(points: List<Offset>) {
                for (index in 1 until points.size) {
                    drawLine(
                        color = writingColor,
                        start = points[index - 1],
                        end = points[index],
                        strokeWidth = size.minDimension * 0.035f,
                        cap = StrokeCap.Round,
                    )
                }
            }

            completedStrokes.forEach(::drawStroke)
            drawStroke(activeStroke)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    completedStrokes.clear()
                    activeStroke = emptyList()
                    passed = false
                    feedback = null
                },
            ) {
                Text("Clear")
            }

            Button(
                modifier = Modifier.weight(1f),
                enabled = passed,
                onClick = onContinue,
            ) {
                Text("Continue")
            }
        }
    }
}

private fun traceFeedback(reason: String): String = when (reason) {
    "too_short" -> "Draw the whole ㄱ in one continuous stroke."
    "start_position" -> "Start closer to the dot."
    "corner_position" -> "Turn downward near the top-right corner."
    "move_right_first" -> "First move to the right."
    "move_down_second" -> "After the corner, move down."
    "vertical_alignment" -> "Keep the second part more vertical."
    "guide_deviation" -> "Stay closer to the guide line."
    "end_position" -> "Finish near the bottom of the guide."
    else -> "Try tracing ㄱ again."
}
