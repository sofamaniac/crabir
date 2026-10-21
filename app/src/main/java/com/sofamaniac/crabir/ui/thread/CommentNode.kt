package com.sofamaniac.crabir.ui.thread

import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.ui.thread.comment.CommentView
import com.sofamaniac.crabir.ui.votable.CommentEvent

@Composable
fun CommentNode(
    depth: Int,
    comment: CommentType.Comment,
    opened: Boolean,
    children: Int,
    isNew: Boolean,
    onEvent: (CommentEvent) -> Unit,
    enableAnimation: Boolean = true,
) {
    CommentView(
        comment = comment.comment,
        depth = depth,
        children = children,
        opened = opened,
        highlight = isNew,
        enableAnimation = enableAnimation,
        onEvent = onEvent
    )
}

