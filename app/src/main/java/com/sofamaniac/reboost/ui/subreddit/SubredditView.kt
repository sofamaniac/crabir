/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.subreddit

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.reboost.ui.TabBar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditViewer(
    subreddit: String,
    selected: State<Int>,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    viewModel: SubredditViewModel = hiltViewModel<SubredditViewModel, SubredditViewModel.Factory> { factory ->
        factory.create(subreddit)
    },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val topBar = @Composable {
        TopBar(
            subreddit,
            viewModel,
            drawerState,
            scrollBehavior,
        )
    }
    val bottomBar = @Composable {
        TabBar(
            selected,
            onTabReselect = {
                scope.launch {
                    viewModel.listState.animateScrollToItem(0)
                }
            }
        )
    }
    FullFeedView(
        drawerState, topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}

@HiltViewModel(assistedFactory = SubredditViewModel.Factory::class)
class SubredditViewModel @AssistedInject constructor(
    repository: SubredditPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    @Assisted private val subredditName: String
) : PostFeedViewModel(repository, visitedPostsDao) {

    init {
        repository.updateSubreddit(subredditName)
    }

    @AssistedFactory
    interface Factory {
        fun create(subreddit: String): SubredditViewModel
    }

}


