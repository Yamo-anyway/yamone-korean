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

@Composable
fun TraceScreen(
    onContinue: () -> Unit,
) {
    val completedStrokes = remember { mutableStateListOf<List<Offset>>() }
    var activeStroke by remember { mutableStateOf<List<Offset>>(emptyList()) }
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

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1f)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { activeStroke = listOf(it) },
                        onDragEnd = {
                            if (activeStroke.size > 1) {
                                completedStrokes.add(activeStroke)
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
                },
            ) {
                Text("Clear")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = onContinue,
            ) {
                Text("Continue")
            }
        }
    }
}
