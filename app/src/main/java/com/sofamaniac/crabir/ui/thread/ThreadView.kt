/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.thread

import android.util.Log
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
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
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.data.remote.reddit.Kind
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.FullnameKind
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.settings.history.HistoryManager
import com.sofamaniac.crabir.settings.history.SaveToHistory
import com.sofamaniac.crabir.ui.thread.dialog.CopyDialog
import com.sofamaniac.crabir.ui.thread.dialog.ShareMenu
import com.sofamaniac.crabir.ui.thread.dialog.UserMenu
import com.sofamaniac.crabir.ui.thread.topBar.TopBar
import com.sofamaniac.crabir.ui.votable.ReportMenu
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
    val post by viewModel.post.collectAsState()

    SaveToHistory(viewModel.name, post?.over18 ?: true)
    val historyManager: HistoryManager = koinInject()
    val scope = rememberCoroutineScope()
    DisposableEffect(Unit) {
        onDispose {
            val comments = viewModel.comments.value.toList().map { it.name }
            val focusedIndex = viewModel.listState.firstVisibleItemIndex
            val focusedComment = comments.getOrNull(focusedIndex)
            if (focusedComment != null) {
                scope.launch(Dispatchers.IO) {
                    historyManager.updateComments(viewModel.name, comments, focusedComment)
                }
            }
        }
    }

    if (commentsSettings.useVolumeKeyNavigation) {
        VolumeKeyNavigation { viewModel.move(it) }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.loadingState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is LoadingState.Error) {
            snackbarHostState.showSnackbar(
                (uiState as LoadingState.Error).e.message ?: "Unknown error"
            )
        }
    }
    val newComments by viewModel.newComments.collectAsState()
    LaunchedEffect(newComments) {
        if (newComments.isNotEmpty()) {
            snackbarHostState.showSnackbar("${newComments.size} new comments")
        }
    }

    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
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
                        move = { viewModel.move(it) },
                        nextTopLevel = { viewModel.nextTopLevel() },
                        previousTopLevel = { viewModel.previousTopLevel() }
                    )
                }
                ThreadListView(
                    viewModel = viewModel,
                    comment = comment,
                    context = context,
                )
            }
            Dialogs(viewModel)
        }
    }
}

@Composable
fun Dialogs(viewModel: ThreadViewModel) {
    val dialog by viewModel.dialogState.collectAsState()
    when (val dialog = dialog) {
        is Dialog.Copy -> {
            val comment by viewModel.get(dialog.name).collectAsState(null)
            if (comment is CommentType.Comment) {
                val comment = comment as CommentType.Comment
                CopyDialog(comment.comment) {
                    viewModel.dialogState.value = null
                }
            }
        }

        is Dialog.Reply -> {
            ReplyBottomSheet(viewModel)
        }

        is Dialog.Report -> {
            val rules: Rules by viewModel.rules.collectAsState(Rules())
            val kind = when (dialog.name.kind) {
                FullnameKind.Comment -> Kind.Comment
                FullnameKind.Post -> Kind.Link
                else -> Kind.All
            }
            ReportMenu(dialog.name, rules, kind = kind, report = viewModel::report) {
                viewModel.dialogState.value = null
            }
        }

        is Dialog.Share -> {
            val comment by viewModel.get(dialog.name).collectAsState(null)
            if (comment is CommentType.Comment) {
                val comment = comment as CommentType.Comment
                ShareMenu(comment.comment) {
                    viewModel.dialogState.value = null
                }
            }
        }

        is Dialog.User -> {
            UserMenu(dialog.username) {
                viewModel.dialogState.value = null
            }
        }

        null -> {}
        else -> {
            Log.e("ThreadView", "Dialogs: Missing implementation for $dialog")
        }
    }
}
