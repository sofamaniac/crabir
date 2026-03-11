/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.subreddit

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.data.local.dao.VisitedCommunityDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.crabir.ui.TabBar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubredditViewer(
    subreddit: String,
    selected: State<Int>,
    modifier: Modifier = Modifier,
    viewModel: SubredditViewModel = hiltViewModel<SubredditViewModel, SubredditViewModel.Factory> { factory ->
        factory.create(subreddit)
    },
) {
    Log.d("SubredditViewer", "subreddit: $subreddit")
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val topBar = @Composable {
        TopBar(
            subreddit,
            viewModel,
            scrollBehavior,
        )
    }
    val bottomBar = @Composable {
        TabBar(
            selected,
        )
    }
    FullFeedView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}

@HiltViewModel(assistedFactory = SubredditViewModel.Factory::class)
class SubredditViewModel @AssistedInject constructor(
    repository: SubredditPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    visitedCommunityDao: VisitedCommunityDao,
    @Assisted private val subredditName: String
) : PostFeedViewModel(id = subredditName, repository, visitedPostsDao, visitedCommunityDao) {

    init {
        repository.updateSubreddit(subredditName)
    }

    @AssistedFactory
    interface Factory {
        fun create(subreddit: String): SubredditViewModel
    }

}


