/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.HomeRepository
import com.sofamaniac.crabir.ui.TabBar
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val title = stringResource(R.string.Home)
    val params by viewModel.params.collectAsState()
    val topBar = @Composable {
        TopBar(
            title,
            params,
            updateSort = viewModel::updateSort,
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior
        )
    }
    val bottomBar = @Composable {
        TabBar(0, onTabReselect = {
            scope.launch {
                viewModel.listState.animateScrollToItem(0)
            }
        })
    }
    FullFeedView(
        topBar,
        bottomBar,
        viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        feedInfo = null,
    )

}

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: HomeRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditDao,
    viewDao: CommunityViewDao
) : PostFeedViewModel<SubredditData>(
    id = Fullname("_HOME"),
    repository,
    visitedPostsDao,
    communityDao,
    viewDao
)


