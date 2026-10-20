package com.sofamaniac.crabir.ui.thread

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.ui.thread.comment.CommentView
import com.sofamaniac.crabir.ui.thread.comment.CommentViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CommentNode(
    depth: Int,
    comment: CommentType.Comment,
    viewModel: ThreadViewModel,
    enableAnimation: Boolean = true,
) {
    val commentViewModel = koinViewModel<CommentViewModel>(key = comment.name.name) {
        parametersOf(comment)
    }
    val opened by viewModel.openComment.collectAsState()
    val newComments by viewModel.newComments.collectAsState()
    val children = remember(comment.name) { viewModel.getNumberOfChildren(comment.name) }
    CommentView(
        viewModel = commentViewModel,
        depth = depth,
        children = children,
        opened = opened == comment.name,
        highlight = newComments.contains(comment.name),
        toggleComment = { target ->
            viewModel.toggleComment(comment.name, target)
        },
        enableAnimation = enableAnimation,
        startReply = { viewModel.replyTo(comment.name) }
    )
}

