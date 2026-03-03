package com.sofamaniac.reboost.ui.thread

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.LocalTheme
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.domain.model.CommentData
import com.sofamaniac.reboost.ui.Flair
import com.sofamaniac.reboost.ui.cartouche
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized
import com.sofamaniac.reboost.ui.markdown.RedditMarkdown
import com.sofamaniac.reboost.ui.post.DownButton
import com.sofamaniac.reboost.ui.post.SavedButton
import com.sofamaniac.reboost.ui.post.UpButton
import kotlinx.coroutines.flow.map

@Composable
fun CommentNode(
    comment: CommentData,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {


    val context = LocalContext.current
    val showBottomBar by remember(comment.name, context) {
        viewModel.openComment.map { it == comment.name || !enableAnimation }
    }.collectAsState(initial = false)

    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
    //.padding(bottom = 8.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { viewModel.toggleComment(comment.name) }),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        TopRow(comment, modifier = innerModifier)
        Spacer(modifier = Modifier.height(8.dp))
        RedditMarkdown(
            comment.bodyMd,
            modifier = innerModifier,
            mediaMetadata = comment.mediaMetadata,
            key = comment.id
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(showBottomBar) {
            BottomRow(comment, viewModel)
        }
    }
}

@Composable
fun BottomRow(comment: CommentData, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    val likes = comment.relationship.liked
    val saved = comment.relationship.saved
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f)),
        horizontalArrangement = Arrangement.End
    ) {
        UpButton(likes) { viewModel.upvote(comment.name, likes) }
        DownButton(likes) { viewModel.downvote(comment.name, likes) }
        SavedButton(saved) { viewModel.save(comment.name, saved) }
    }
}

@Composable
fun TopRow(comment: CommentData, modifier: Modifier = Modifier) {

    val theme = LocalTheme.current
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

    val navController = LocalNavController.current!!
    val authorModifier = Modifier.clickable {
        navController.navigate(ProfileRoute(comment.author.username))
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (comment.isSubmitter) {
            Text(
                comment.author.username,
                color = Color.White,
                modifier = authorModifier.cartouche(Color(0xFF2196F3))
            )
        } else {
            Text(comment.author.username, color = theme.highlight, modifier = authorModifier)
        }
        Box(modifier = Modifier.weight(10f)) {
            Flair(comment.author.flair)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(rightString, maxLines = 1)
    }
}
