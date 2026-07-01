/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.thread

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.toRoute
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.editor.EditorActions

/**
 * @param comment Focal point of the view
 * @param context If is not null, number of parents to show
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadView(
    modifier: Modifier = Modifier,
    permalink: String? = null,
    comment: String? = null,
    context: Int? = null,
    dismiss: () -> Unit = {},
) {
    val link = permalink
        ?: LocalNavController.current?.currentBackStackEntry?.toRoute<PostRoute>()?.postPermalink

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val viewModel: ThreadViewModel =
        hiltViewModel<ThreadViewModel, ThreadViewModel.Factory>(key = link) { factory ->
            // TODO user setting initial sort
            // TODO remember last set sort
            factory.create(link!!, comment = comment, context = context, initialSort = null)
        }
    val showReplySheet by viewModel.reply.collectAsState()

    Scaffold(
        topBar = { TopBar(viewModel, scrollBehavior, dismiss) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        CommentListRoot(
            viewModel = viewModel,
            comment = comment,
            context = context,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )

        if (showReplySheet != null) {
            ReplyBottomSheet(viewModel)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReplyBottomSheet(viewModel: ThreadViewModel) {
    val parentName by viewModel.reply.collectAsState()
    val post by viewModel.post.collectAsState(null)
    val comments by viewModel.comments.collectAsState()
    val parent = if (parentName == post?.name) post else comments.find { it.name == parentName }
    if (parent == null) return
    val textFieldState = rememberTextFieldState()
    ModalBottomSheet(
        modifier = Modifier
            .imePadding()
            .imeNestedScroll()
            .fillMaxWidth(),
        onDismissRequest = { viewModel.replyTo(null) }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { viewModel.replyTo(null) }) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    viewModel.submitComment(parent.name, textFieldState.text.toString())
                }) {
                    Text("Submit")
                }
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                state = textFieldState,
                label = { Text("Reply to ${parent.author?.username ?: "user"}") }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EditorActions(textFieldState)
            }
        }
    }
}