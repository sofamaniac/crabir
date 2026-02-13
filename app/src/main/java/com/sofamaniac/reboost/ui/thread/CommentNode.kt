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
import com.sofamaniac.reboost.domain.model.CommentData
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized
import com.sofamaniac.reboost.ui.markdown.SimpleMarkdown
import com.sofamaniac.reboost.ui.post.DownButton
import com.sofamaniac.reboost.ui.post.SavedButton
import com.sofamaniac.reboost.ui.post.UpButton
import kotlinx.coroutines.flow.map

@Composable
fun CommentNode(comment: CommentData, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    val showBottomBar by viewModel.openComment.map { it == comment.id }
        .collectAsState(initial = false)
    Column(
        modifier = modifier.clickable(onClick = { viewModel.toggleComment(comment.id) })
    ) {
        TopRow(comment)
        SimpleMarkdown(
            comment.bodyMd,
        )
        AnimatedVisibility(showBottomBar) {
            BottomRow(comment, viewModel)
        }
    }
}

@Composable
fun BottomRow(comment: CommentData, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    val likes = comment.relationship.liked
    val saved = comment.relationship.saved
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        UpButton(likes) { viewModel.upvote(comment.name, likes) }
        DownButton(likes) { viewModel.downvote(comment.name, likes) }
        SavedButton(saved) { viewModel.save(comment.name, saved) }
    }
}

@Composable
fun TopRow(comment: CommentData, modifier: Modifier = Modifier) {
    val timeString = formatElapsedTimeLocalized(comment.createdUtc)
    val delta = when (comment.relationship.liked) {
        true -> 1
        false -> -1
        else -> 0
    }
    val rightString = buildAnnotatedString {
        append("${comment.score.ups + delta}")
        append(" · ")
        append(timeString)
    }
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(comment.author.username, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.weight(1f))
        Text(rightString)
    }
}
