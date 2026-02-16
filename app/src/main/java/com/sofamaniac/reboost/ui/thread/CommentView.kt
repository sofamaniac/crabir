package com.sofamaniac.reboost.ui.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.domain.model.CommentType

@Composable
fun CommentView(comment: CommentType, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(comment.depth) {
            VerticalDivider(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
            )

        }
        if (comment is CommentType.Comment) {
            CommentNode(
                comment.comment,
                viewModel = viewModel,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            Text("TODO MORE VIEW")
        }
    }
}