package com.sofamaniac.crabir.ui.thread

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.ui.Flair
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import com.sofamaniac.crabir.ui.post.VotableInteraction
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.SavedButton
import com.sofamaniac.crabir.ui.votable.ScoreString
import com.sofamaniac.crabir.ui.votable.UpButton
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

interface CommentViewModelInterface : VotableInteraction {
    val openComment: StateFlow<Fullname?>
    fun submitComment(parent: Fullname, body: String)
}

@Composable
fun CommentNode(
    comment: CommentData,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {
    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
    Column {
        ThemedCard(
            shape = RoundedCornerShape(0),
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { if (!comment.collapsed) viewModel.toggleComment(comment.name) },
                    onLongClick = { viewModel.collapseComment(comment.name, !comment.collapsed) },
                    onLongClickLabel = "Collapse comment"
                ),
        ) {
            if (comment.collapsed) {
                CollapsedComment(comment, modifier = innerModifier.padding(vertical = 8.dp))
            } else {
                OpenedComment(comment, viewModel, modifier = innerModifier, enableAnimation)
            }
        }
        AnimatedVisibility(!comment.collapsed) {
            Column {
                for (reply in comment.replies) {
                    when (reply) {
                        is CommentType.Comment -> CommentNode(
                            reply.comment,
                            viewModel,
                            modifier = modifier.depthIndent(1),
                        )

                        is CommentType.More -> MoreViewer(
                            reply,
                            viewModel,
                            modifier = modifier.depthIndent(1)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CollapsedComment(
    comment: CommentData,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val timeString = formatElapsedTimeLocalized(comment.createdUtc)
    val rightString = buildAnnotatedString {
        append("· ")
        append(timeString)
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("[+] ${comment.author.username}", color = theme.secondaryText)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "+${comment.replies.size}",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.cartouche(backgroundColor = Color.Green)
        )
        ScoreString(
            comment.score.score,
            comment.relationship.liked,
            hidden = comment.score.hideScore
        )
        Text(rightString)
    }
}

@Composable
fun ColumnScope.OpenedComment(
    comment: CommentData,
    viewModel: CommentViewModelInterface,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {
    val context = LocalContext.current
    val showBottomBar by remember(comment.name, context) {
        viewModel.openComment.map { it == comment.name || !enableAnimation }
    }.collectAsState(initial = !enableAnimation)

    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
    //.padding(bottom = 8.dp)
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

@Composable
fun BottomRow(
    comment: CommentData,
    viewModel: CommentViewModelInterface,
    modifier: Modifier = Modifier
) {
    val likes = comment.relationship.liked
    val saved = comment.relationship.saved

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f)),
        horizontalArrangement = Arrangement.End
    ) {
        UpButton(likes, onClick = { viewModel.upvote(comment.name) })
        DownButton(likes, onClick = { viewModel.downvote(comment.name) })
        SavedButton(saved, onClick = { viewModel.save(comment.name, !saved) })
        ReplyButton(parentId = comment.name, submitComment = { name, comment ->
            viewModel.submitComment(name, comment)
        }) {
            ThemedCard(modifier = Modifier.padding(all = 16.dp)) {
                Text(comment.author.username, modifier = modifier)
                RedditMarkdown(
                    comment.bodyMd,
                    maxLines = 5,
                    modifier = modifier
                )
            }
        }
        if (BuildConfig.DEBUG) {
            IconButton(onClick = {
                Log.d("CommentNode", "$comment")
            }) { Icon(Icons.Default.BugReport, contentDescription = null) }
        }
    }
}

@Composable
fun TopRow(comment: CommentData, modifier: Modifier = Modifier) {

    val theme = LocalTheme.current
    val timeString = formatElapsedTimeLocalized(comment.createdUtc)
    val rightString = buildAnnotatedString {
        append("· ")
        append(timeString)
    }

    val navController = LocalNavController.current!!
    val authorModifier = Modifier.clickable {
        navController.navigate(
            ProfileRoute(
                comment.author.username,
                ProfileTabs.Overview
            )
        )
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
        ScoreString(
            comment.score.score,
            comment.relationship.liked,
            hidden = comment.score.hideScore
        )
        Text(rightString, maxLines = 1)
    }
}
