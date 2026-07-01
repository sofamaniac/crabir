package com.sofamaniac.crabir.ui.editor.commentEditor

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.editor.Editor
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown

@Composable
fun CommentEditor(
    parent: Fullname,
    viewModel: CommentEditorViewModel = hiltViewModel<CommentEditorViewModel, CommentEditorViewModel.Factory> { factory ->
        factory.create(parent.name)
    }
) {
    val navController = LocalNavController.current
    val parentData by viewModel.parentData.collectAsState(null)
    if (parentData == null) return
    Editor(
        state = viewModel.replyState,
        label = { Text("Type comment") },
        topBar = {
            TopAppBar(
                title = { Text("Comment") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController?.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.submitComment()
                        navController?.popBackStack()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Reply"
                        )
                    }
                }
            )
        }) {
        when (parentData) {
            is PostData -> PostView(parentData as PostData)
            is CommentData -> CommentView(parentData as CommentData)
            is CommentType.Comment -> {
                val comment = (parentData as CommentType.Comment).comment
                CommentView(comment)
            }
        }
    }
}

@Composable
internal fun CommentView(comment: CommentData, modifier: Modifier = Modifier) {
    ThemedCard() {
        Text(comment.author.username, modifier = modifier)
        RedditMarkdown(
            comment.body,
            maxLines = 5,
            modifier = modifier,
            key = comment.name
        )
    }
}

@Composable
internal fun PostView(post: PostData, modifier: Modifier = Modifier) {
    ThemedCard(modifier = Modifier.padding(all = 16.dp)) {
        Text(post.author.username, modifier = modifier)
        Text(post.title, modifier = modifier)
        RedditMarkdown(
            post.selftext.markdown,
            maxLines = 5,
            modifier = modifier,
        )
    }
}