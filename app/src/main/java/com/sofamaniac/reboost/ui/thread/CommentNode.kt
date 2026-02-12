package com.sofamaniac.reboost.ui.thread

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized
import com.sofamaniac.reboost.ui.markdown.SimpleMarkdown
import com.sofamaniac.reboost.ui.post.DownButton
import com.sofamaniac.reboost.ui.post.SavedButton
import com.sofamaniac.reboost.ui.post.UpButton
import kotlinx.coroutines.flow.map

@Composable
fun CommentNode(comment: Thing.Comment, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    val showBottomBar by viewModel.openComment.map { it == comment.data.id }
        .collectAsState(initial = false)
    Column(
        modifier = modifier.clickable(onClick = { viewModel.toggleComment(comment.data.id) })
    ) {
        TopRow(comment)
        SimpleMarkdown(
            comment.data.body,
        )
        AnimatedVisibility(showBottomBar) {
            BottomRow(comment, viewModel)
        }
    }
}

@Composable
fun BottomRow(comment: Thing.Comment, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        UpButton(comment.data.likes) { viewModel.upvote(comment.data.name) }
        DownButton(comment.data.likes) { viewModel.downvote(comment.data.name) }
        SavedButton(comment.data.saved) { viewModel.save(comment.data.name) }
    }
}

@Composable
fun TopRow(comment: Thing.Comment, modifier: Modifier = Modifier) {
    val timeString = formatElapsedTimeLocalized(comment.data.created_utc)
    val rightString = buildAnnotatedString {
        append("${comment.data.ups}")
        append(" · ")
        append(timeString)
    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(comment.data.author, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.weight(1f))
        Text(rightString)
    }
}
