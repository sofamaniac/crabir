package com.sofamaniac.crabir.ui.thread

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.LocalCommentsSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.settings.theme.ADMIN_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.AUTHOR_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.MODERATOR_CARTOUCHE_COLOR
import com.sofamaniac.crabir.ui.components.Flair
import com.sofamaniac.crabir.ui.components.ThemedCard
import com.sofamaniac.crabir.ui.components.cartouche
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.richtext.Richtext
import com.sofamaniac.crabir.ui.thread.dialog.MoreOptionButton
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.SavedButton
import com.sofamaniac.crabir.ui.votable.ScoreString
import com.sofamaniac.crabir.ui.votable.UpButton
import com.sofamaniac.crabir.ui.votable.VotableInteraction
import com.sofamaniac.crabir.ui.votable.VotableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf

interface CommentViewModelInterface : VotableInteraction {
    val openComment: StateFlow<Fullname?>

    fun replyTo(name: Fullname?)
    fun submitComment(parent: Fullname, body: String, account: RedditAccount?)

    fun collapseComment(name: Fullname, collapsed: Boolean)

    fun closeComment(name: Fullname)

}

@KoinViewModel
open class CommentViewModel(
    @InjectedParam comment: CommentType.Comment,
    repository: CommentsRepository,
) :
    VotableViewModel<CommentType>(
        subreddit = comment.comment.subredditInfo.subredditPrefixed,
        fullname = comment.name,
        initialData = comment,
        repository = repository
    ) {

    override val votable: StateFlow<CommentType.Comment?> =
        super.votable.map { it as? CommentType.Comment }.stateIn(
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = comment
        )

    open fun collapse(collapsed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val comment = repository.get(fullname).first()
            if (comment is CommentType.Comment) {
                val updated = comment.copy(comment = comment.comment.copy(collapsed = collapsed))
                repository.update(updated)
            }
        }
    }
}

@Composable
fun CommentContent(
    comment: CommentType.Comment,
    viewModel: CommentViewModel,
    opened: Boolean,
    toggleComment: (Boolean) -> Unit,
    startReply: () -> Unit,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true,
) {
    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
    val theme = LocalTheme.current
    val commentOpt by viewModel.votable.collectAsState()
    if (commentOpt == null) return
    val comment = commentOpt!!.comment

    Column(modifier = modifier) {
        if (comment.depth == 0) HorizontalDivider()
        ThemedCard(
            roundedCorners = false,
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.cardBackground)
                .depthIndent(comment.depth.coerceAtLeast(0))
                .combinedClickable(
                    onClick = { if (!comment.collapsed) toggleComment(!opened) },
                    onLongClick = { viewModel.collapse(!comment.collapsed) },
                    onLongClickLabel = "Collapse comment"
                ),
        ) {
            if (comment.collapsed) {
                CollapsedComment(comment, modifier = innerModifier.padding(vertical = 8.dp))
            } else {
                OpenedComment(
                    viewModel,
                    opened = opened,
                    toggleComment = toggleComment,
                    modifier = innerModifier,
                    enableAnimation = enableAnimation,
                    startReply = startReply
                )
            }
        }
    }
}

@Composable
fun CommentNode(
    comment: CommentType.Comment,
    viewModel: ThreadViewModel,
    enableAnimation: Boolean = true,
) {
    val commentViewModel = koinViewModel<CommentViewModel>(key = comment.name.name) {
        parametersOf(comment)
    }
    val opened by viewModel.openComment.collectAsState()
    CommentContent(
        comment,
        commentViewModel,
        opened == comment.name,
        toggleComment = { target ->
            viewModel.toggleComment(comment.name, target)
        },
        enableAnimation = enableAnimation,
        startReply = { viewModel.replyTo(comment.name) }
    )
}

fun LazyListScope.commentNode(
    comment: CommentType.Comment,
    viewModel: ThreadViewModel,
    enableAnimation: Boolean = true,
) {
    item(key = comment.name) {
        CommentNode(comment, viewModel, enableAnimation)
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
            //"+${comment.replies.size}",
            "+${comment.replies}",
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
fun CommentInner(comment: CommentData, modifier: Modifier = Modifier) {
    Column(modifier.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TopRow(comment)
        Richtext(
            comment.richtext,
            mediaMetadata = comment.mediaMetadata,
            threadId = comment.parentInfo.name.name.split("_").last()
        )
    }
}

@Composable
fun OpenedComment(
    viewModel: CommentViewModel,
    opened: Boolean,
    toggleComment: (Boolean) -> Unit,
    startReply: () -> Unit,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true,
) {
    val showBottomBar = opened || !enableAnimation
    val commentsSettings = LocalCommentsSettings.current
    val commentOuter by viewModel.votable.collectAsState()
    val comment = (commentOuter)?.comment ?: return

    Column(modifier) {
        CommentInner(comment)
        AnimatedVisibility(showBottomBar) {
            BottomRow(comment, viewModel, startReply = startReply) {
                if (commentsSettings.hideButtonsAfterVote) {
                    toggleComment(false)
                }
            }
        }
    }
}

@Composable
fun BottomRow(
    comment: CommentData,
    viewModel: CommentViewModel,
    startReply: () -> Unit,
    modifier: Modifier = Modifier,
    onAction: () -> Unit,
) {
    val likes = comment.relationship.liked
    val saved = comment.relationship.saved
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f)),
        horizontalArrangement = Arrangement.End
    ) {
        UpButton(likes, onClick = {
            viewModel.upvote(comment.name)
            onAction()
        })
        DownButton(likes, onClick = {
            viewModel.downvote(comment.name)
            onAction()
        })
        SavedButton(saved, onClick = {
            viewModel.save(comment.name, !saved, false)
            onAction()
        })
        ReplyButton(startReply = startReply)
        MoreOptionButton(comment, viewModel)
        if (BuildConfig.DEBUG) {
            IconButton(onClick = {
                Log.d("CommentNode", "$comment")
            }) {
                Icon(Icons.Default.BugReport, contentDescription = null)
            }
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
