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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.model.State
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.settings.theme.ADMIN_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.AUTHOR_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.MODERATOR_CARTOUCHE_COLOR
import com.sofamaniac.crabir.ui.Flair
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.cartouche
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.SavedButton
import com.sofamaniac.crabir.ui.votable.ScoreString
import com.sofamaniac.crabir.ui.votable.UpButton
import com.sofamaniac.crabir.ui.votable.VotableInteraction
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

interface CommentViewModelInterface : VotableInteraction {
    val openComment: StateFlow<Fullname?>
    fun submitComment(parent: Fullname, body: String)

    fun getMarkdownState(name: Fullname): StateFlow<State>
}

fun LazyListScope.replies(
    replies: List<CommentType>,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true,
) {
    replies.forEach { comment ->
        when (comment) {
            is CommentType.Comment -> commentNode(
                comment.comment,
                viewModel,
                modifier,
                enableAnimation
            )

            is CommentType.More -> MoreNode(comment, viewModel, modifier)
        }
    }
}

@Composable
fun CommentContent(
    comment: CommentData,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {

    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
    Column {
        if (comment.depth == 0) HorizontalDivider()
        ThemedCard(
            shape = RoundedCornerShape(0),
            modifier = modifier
                .fillMaxWidth()
                .depthIndent(comment.depth.coerceAtLeast(0))
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
    }
}

fun LazyListScope.commentNode(
    comment: CommentData,
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true
) {
    item {
        CommentContent(comment, viewModel, modifier, enableAnimation)
    }
    //AnimatedVisibility(!comment.collapsed) {
    if (!comment.collapsed) {
        replies(comment.replies, viewModel, modifier, enableAnimation)
    }
    //}
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
    val authorString = buildAnnotatedString {
        append("[+] ")
        if (comment.distinguished == "moderator") {
            withStyle(SpanStyle(background = Color(0xFFB2FF59))) {
                append(comment.author.username)
            }
        } else {
            append(comment.author.username)
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(authorString, color = theme.secondaryText)
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
    val markdownState by viewModel.getMarkdownState(comment.name).collectAsState()

    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
    //.padding(bottom = 8.dp)
    Spacer(modifier = Modifier.height(8.dp))
    TopRow(comment, modifier = innerModifier)
    Spacer(modifier = Modifier.height(8.dp))
    RedditMarkdown(
        markdownState,
        modifier = innerModifier,
        mediaMetadata = comment.mediaMetadata,
        //key = comment.id,
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
    val markdownState by viewModel.markdown.collectAsState()
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
                    markdownState,
                    maxLines = 5,
                    modifier = modifier,
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
    val authorString = comment.author.username
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (comment.isSubmitter) {
            Text(
                authorString,
                color = Color.White,
                modifier = authorModifier.cartouche(AUTHOR_CARTOUCHE_COLOR)
            )
        } else if (comment.distinguished == "moderator") {
            Text(
                authorString,
                color = Color.White,
                modifier = authorModifier.cartouche(MODERATOR_CARTOUCHE_COLOR)
            )
        } else if (comment.distinguished == "admin") {
            Text(
                authorString,
                color = Color.White,
                modifier = authorModifier.cartouche(ADMIN_CARTOUCHE_COLOR)
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
