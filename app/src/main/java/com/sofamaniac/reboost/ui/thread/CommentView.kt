package com.sofamaniac.reboost.ui.thread

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.model.CommentType

@Composable
fun CommentView(comment: CommentType, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                for (reply in comment.comment.replies) {
                    CommentView(reply, viewModel, modifier)
                }
            } else {
                Text("TODO MORE VIEW")
            }
        }
    }
    if (comment.depth == 0) {
        HorizontalDivider()
    }
}

fun flattenComments(comment: Thing, depth: Int = 0): List<Pair<Thing, Int>> {
    return when (comment) {
        is Thing.Comment -> {
            listOf(Pair(comment, depth)) + comment.data.replies.flatMap {
                flattenComments(
                    it,
                    depth + 1
                )
            }
        }

        is Thing.More -> {
            listOf(Pair(comment, depth))
        }

        else -> {
            Log.e("flattenComments", "Unknown comment type: ${comment.javaClass.name}")
            emptyList()
        }

    }
}
