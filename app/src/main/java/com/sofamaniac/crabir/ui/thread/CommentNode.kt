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
import com.sofamaniac.crabir.LocalNavController
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.ProfileRoute
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
                CollapsedComment(comment, modifier = innerModifier)
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
        append(" · ")
        append(timeString)
    }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text("[+] ${comment.author.username}", color = theme.secondaryText)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "+${comment.replies.size}",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.cartouche(backgroundColor = Color.Green)
        )
        ScoreString(comment.score.score, comment.relationship.liked)
        Text(rightString)
    }
}

@Composable
private fun ColumnScope.OpenedComment(
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
fun BottomRow(comment: CommentData, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    val likes = comment.relationship.liked
    val saved = comment.relationship.saved
    val votable = remember(comment) {
        object : VotableInteraction {
            override val likes: Flow<Boolean?> = flowOf(likes)
            override val saved: Flow<Boolean> = flowOf(saved)

            override fun upvote() {
                viewModel.upvote(comment.name, likes)
            }

            override fun downvote() {
                viewModel.downvote(comment.name, likes)
            }

            override fun save(target: Boolean) {
                viewModel.save(comment.name, saved)
            }
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f)),
        horizontalArrangement = Arrangement.End
    ) {
        UpButton(votable)
        DownButton(votable)
        SavedButton(votable)
        IconButton(onClick = {
            Log.d("CommentNode", "$comment")
        }) { Icon(Icons.Default.BugReport, contentDescription = null) }
    }
}

@Composable
fun TopRow(comment: CommentData, modifier: Modifier = Modifier) {

    val theme = LocalTheme.current
    val timeString = formatElapsedTimeLocalized(comment.createdUtc)
    val rightString = buildAnnotatedString {
        append(" · ")
        append(timeString)
    }

    val navController = LocalNavController.current!!
    val authorModifier = Modifier.clickable {
        navController.navigate(
            ProfileRoute(
                comment.author.username,
                ProfileTabs.Overview.toString()
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
        ScoreString(comment.score.ups, comment.relationship.liked, hidden = comment.score.hideScore)
        Text(rightString, maxLines = 1)
    }
}
