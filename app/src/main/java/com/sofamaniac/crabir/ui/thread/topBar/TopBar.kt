package com.sofamaniac.crabir.ui.thread.topBar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SubredditInfoRoute
import com.sofamaniac.crabir.settings.theme.rememberTopAppBarColors
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.post.dialog.ShareMenu
import com.sofamaniac.crabir.ui.thread.SortMenu
import com.sofamaniac.crabir.ui.thread.ThreadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: ThreadViewModel,
    scrollBehavior: TopAppBarScrollBehavior?,
    dismiss: () -> Unit,
) {
    val sort: Sort? by viewModel.sort.collectAsState()
    val post by viewModel.post.collectAsState()
    val navController = LocalNavController.current
    var showShareMenu by remember { mutableStateOf(false) }
    TopAppBar(
        colors = rememberTopAppBarColors(),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            BackButton { dismiss() }
        },
        title = {
            Column {
                Text(
                    stringResource(R.string.thread_top_bar_title),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    stringResource((sort ?: Sort.Best).representation),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        actions = {
            //Icon(Icons.Default.Search, "Search comments")
            SortMenu(viewModel)
            MoreOptionsMenu(
                refresh = viewModel::refresh,
                reply = {
                    post?.let { post ->
                        viewModel.replyTo(post.name)
                    }
                },
                goToCommunityInfo = {
                    post?.let { post ->
                        navController?.navigate(SubredditInfoRoute(post.subreddit.subredditPrefixed))
                    }
                },
                share = { showShareMenu = true },
            )
        }
    )

    if (showShareMenu) {
        post?.let { post ->
            ShareMenu(post) {
                showShareMenu = false
            }
        }
    }
}
