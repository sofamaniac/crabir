package com.sofamaniac.reboost.ui.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.domain.model.CommentType

@Composable
fun CommentView(comment: CommentType, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        //.padding(horizontal = 16.dp)
        //.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        if (comment is CommentType.Comment) {
            CommentNode(
                comment.comment,
                viewModel = viewModel,
                modifier = Modifier.depthIndent(comment.depth, color = Color.Gray)
            )
        } else {
            Text("TODO MORE VIEW")
        }
    }
}

private fun Modifier.depthIndent(
    depth: Int,
    color: Color = Color.Gray,
    lineWidth: Dp = 1.dp,
    spacing: Dp = 8.dp,
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