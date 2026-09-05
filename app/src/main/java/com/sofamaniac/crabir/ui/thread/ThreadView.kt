/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.thread

import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingToolbarDefaults.ScreenOffset
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.ViewCompat
import androidx.navigation.toRoute
import com.sofamaniac.crabir.LocalCommentsSettings
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.HistoryManager
import com.sofamaniac.crabir.ui.SaveToHistory
import com.sofamaniac.crabir.ui.editor.AccountSelector
import com.sofamaniac.crabir.ui.editor.EditorActions
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
    modifier: Modifier = Modifier,
    permalink: String? = null,
    comment: String? = null,
    context: Int? = null,
    dismiss: () -> Unit = {},
) {
    val link = permalink
        ?: LocalNavController.current?.currentBackStackEntry?.toRoute<PostRoute>()?.postPermalink

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val commentsSettings = LocalCommentsSettings.current
    val viewModel: ThreadViewModel = koinViewModel(key = permalink) {
        parametersOf(link!!, comment, context, commentsSettings)
    }
    val showReplySheet by viewModel.reply.collectAsState()

    SaveToHistory(viewModel.name)
    val historyManager: HistoryManager = koinInject()
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    BackHandler {
        val comments = viewModel.comments.value.toList().map { it.id }
        val focusedIndex = viewModel.listState.firstVisibleItemIndex
        val focusedComment = comments.getOrNull(focusedIndex)
        if (focusedComment != null) {
            scope.launch(Dispatchers.IO) {
                historyManager.updateComments(viewModel.name, comments, focusedComment)
                val entity = historyManager.history.getPost(viewModel.name)
                Log.d("ThreadView", "Disposed: $entity")
            }
        }
        navController?.popBackStack()
    }

    val comments by viewModel.comments.collectAsState()

    LaunchedEffect(comments) {
        if (viewModel.initialLoad || comments.count() == 0) return@LaunchedEffect
        val comments = viewModel.comments.value.toList().map { it.id }
        val entity = historyManager.history.getPost(viewModel.name)
        if (entity?.focusedComment != null) {
            val index = comments.indexOf(entity.focusedComment)
            if (index > 0) {
                viewModel.listState.scrollToItem(index)
            }
        }
        viewModel.initialLoad = true
    }

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


    Scaffold(
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReplyBottomSheet(viewModel: ThreadViewModel) {
    val parentName by viewModel.reply.collectAsState()
    val post by viewModel.post.collectAsState(null)
    val comments by viewModel.comments.collectAsState()
    val parent = if (parentName == post?.name) post else comments.find { it.name == parentName }
    if (parent == null) return
    val textFieldState = rememberTextFieldState()
    val initialAccount = LocalRedditAccount.current
    var selectedAccount by remember { mutableStateOf(initialAccount) }
    val accounts by viewModel.accounts.collectAsState(emptyList())
    val theme = LocalTheme.current
    ModalBottomSheet(
        containerColor = theme.cardBackground,
        modifier = Modifier
            .imePadding()
            .imeNestedScroll()
            .fillMaxWidth(),
        onDismissRequest = { viewModel.replyTo(null) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { viewModel.replyTo(null) }) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = {
                    viewModel.submitComment(
                        parent.name,
                        textFieldState.text.toString(),
                        account = selectedAccount
                    )
                }) {
                    Text("Submit")
                }
            }
            AccountSelector(accounts, selectedAccount) { newId ->
                selectedAccount = accounts.find { it.id == newId }!!
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

@Composable
fun Toolbar(modifier: Modifier = Modifier, move: (Int) -> Unit) {
    HorizontalFloatingToolbar(
        expanded = true, modifier = modifier, leadingContent = {
            IconButton(onClick = { move(1) }) {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Next comment")
            }
        },
        trailingContent = {
            IconButton(onClick = { move(-1) }) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Previous comment")
            }
        }) {
        Spacer(modifier = Modifier.width(24.dp))
    }
}

@Composable
fun VolumeKeyNavigation(move: (Int) -> Unit) {
    val context = LocalContext.current
    val view = LocalView.current

    DisposableEffect(context) {
        val keyEventDispatcher = ViewCompat.OnUnhandledKeyEventListenerCompat { _, event ->
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    move(-1)
                    true
                }

                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    move(1)
                    true
                }

                else -> false
            }
        }
        ViewCompat.addOnUnhandledKeyEventListener(view, keyEventDispatcher)
        onDispose {
            ViewCompat.removeOnUnhandledKeyEventListener(view, keyEventDispatcher)
        }
    }

}
