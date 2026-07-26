/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.reddit.HOME
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.HomeRepository
import com.sofamaniac.crabir.ui.TabBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val title = stringResource(R.string.Home)
    val params by viewModel.params.collectAsState()
    val entity =
        LocalViewSettings.current.rememberedViews[HOME] ?: defaultCommunityEntity(HOME, title)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val topBar = @Composable {
        TopBar(
            title,
            params,
            slug = HOME,
            disableInfo = true,
            updateSort = viewModel::updateSort,
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            openDrawer = { scope.launch { drawerState.open() } },
            entity = entity
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
        drawerState = drawerState,
        communityEntity = entity,
    )

}

@KoinViewModel
class HomeViewModel(
    repository: HomeRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditRepository,
) : PostFeedViewModel<SubredditData>(
    repository,
    visitedPostsDao,
    communityDao,
) {
    override suspend fun createViewEntity(name: String): CommunityViewEntity {
        return CommunityViewEntity(name = name, displayName = "Home")
    }
}


