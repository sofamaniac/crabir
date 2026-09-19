/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.thread

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.zIndex
import com.sofamaniac.crabir.LocalCommentsSettings
import com.sofamaniac.crabir.settings.history.HistoryManager
import com.sofamaniac.crabir.settings.history.SaveToHistory
import com.sofamaniac.crabir.ui.thread.topBar.TopBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

/**
 * @param comment Focal point of the view
 * @param context If is not null, number of parents to show
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadView(
    permalink: String,
    modifier: Modifier = Modifier,
    comment: String? = null,
    context: Int? = null,
    dismiss: () -> Unit = {},
) {

    val commentsSettings = LocalCommentsSettings.current
    val viewModel: ThreadViewModel = koinViewModel(key = permalink) {
        parametersOf(permalink, comment, context, commentsSettings)
    }
    ThreadView(
        viewModel = viewModel,
        comment = comment,
        context = context,
        dismiss = dismiss,
        modifier = modifier,
    )

}

/**
 * @param comment Focal point of the view
 * @param context If is not null, number of parents to show
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadView(
    viewModel: ThreadViewModel,
    modifier: Modifier = Modifier,
    comment: String? = null,
    context: Int? = null,
    dismiss: () -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val commentsSettings = LocalCommentsSettings.current
    val showReplySheet by viewModel.reply.collectAsState()
    val post by viewModel.post.collectAsState()

    SaveToHistory(viewModel.name, post?.over18 ?: true)
    val historyManager: HistoryManager = koinInject()
    val scope = rememberCoroutineScope()
    BackHandler {
        val comments = viewModel.comments.value.toList().map { it.name }
        val focusedIndex = viewModel.listState.firstVisibleItemIndex
        val focusedComment = comments.getOrNull(focusedIndex)
        if (focusedComment != null) {
            scope.launch(Dispatchers.IO) {
                historyManager.updateComments(viewModel.name, comments, focusedComment)
            }
        }
        dismiss()
    }

    val comments by viewModel.comments.collectAsState()

    fun move(offset: Int) {
        val index = viewModel.listState.firstVisibleItemIndex
        if (index + offset < 0 || index + offset >= comments.count()) return
        scope.launch {
            viewModel.listState.animateScrollToItem(index + offset)
        }
    }

    if (commentsSettings.useVolumeKeyNavigation) {
        VolumeKeyNavigation { move(it) }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is UiState.Error) {
            snackbarHostState.showSnackbar((uiState as UiState.Error).e.message ?: "Unknown error")
        }
    }
    val newComments by viewModel.newComments.collectAsState()
    LaunchedEffect(newComments) {
        if (newComments.isNotEmpty()) {
            snackbarHostState.showSnackbar("${newComments.size} new comments")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { TopBar(viewModel, scrollBehavior, dismiss) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            if (commentsSettings.showNavigationBar) {
                Toolbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = -ScreenOffset)
                        .zIndex(1f),
                    move = { move(it) },
                )
            }
            CommentListRoot(
                viewModel = viewModel,
                comment = comment,
                context = context,
            )

            if (showReplySheet != null) {
                ReplyBottomSheet(viewModel)
            }
        }
    }
}
