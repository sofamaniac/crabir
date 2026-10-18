package com.sofamaniac.crabir.ui.thread.comment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalCommentsSettings
import com.sofamaniac.crabir.domain.model.votable.CommentData
import com.sofamaniac.crabir.ui.richtext.Richtext

@Composable
fun CommentContent(comment: CommentData, modifier: Modifier = Modifier) {
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

    Column {
        CommentContent(comment, modifier = modifier)
        AnimatedVisibility(showBottomBar) {
            BottomRow(comment, viewModel, startReply = startReply) {
                if (commentsSettings.hideButtonsAfterVote) {
                    toggleComment(false)
                }
            }
        }
    }
}
