package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun Modifier.depthIndent(
    depth: Int,
    color: Color = Color.Gray,
    lineWidth: Dp = 1.dp,
    spacing: Dp = 16.dp,
): Modifier {
    return this
        .drawBehind {
            val lineWidthPx = lineWidth.toPx()
            val spacingPx = spacing.toPx()
            repeat(depth) { i ->
                val x = spacingPx * (i + 1) - lineWidthPx / 2
                drawLine(
                    color = color,
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = lineWidthPx
                )
            }
        }
        .padding(start = spacing * depth)
}