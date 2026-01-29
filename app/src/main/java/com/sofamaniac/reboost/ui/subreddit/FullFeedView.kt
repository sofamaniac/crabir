package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.ui.thread.ThreadView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullFeedView(
    drawerState: DrawerState,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: PostFeedViewModel,
    modifier: Modifier = Modifier,
) {
    Box {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            modifier = modifier,
        ) { innerPadding ->
            PostFeedViewer(
                viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
        if (viewModel.currentPost != null) {
            val swipeToDismissBoxState = rememberSwipeToDismissBoxState()
            SwipeToDismissBox(
                state = swipeToDismissBoxState,
                enableDismissFromEndToStart = false,
                backgroundContent = {},
                onDismiss = {
                    viewModel.currentPost = null
                }
            ) {
                ThreadView(
                    permalink = viewModel.currentPost!!,
                    dismiss = { viewModel.currentPost = null }
                )
            }
        }
    }
}
