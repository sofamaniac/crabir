package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sofamaniac.reboost.FullscreenHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullFeedView(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: FeedViewModelInterface,
    modifier: Modifier = Modifier,
) {
    FullscreenHandler {
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
        }
    }
}
