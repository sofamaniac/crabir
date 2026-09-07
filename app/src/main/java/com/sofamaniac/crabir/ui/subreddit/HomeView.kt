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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalFeedSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.reddit.HOME
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.HomeRepository
import com.sofamaniac.crabir.ui.TabBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.Home)
    val feedSettings = LocalFeedSettings.current
    val defaultEntity: CommunityViewEntity =
        getCommunityViewEntity(
            HOME,
            title,
            defaultSort = feedSettings.homeSort,
            defaultTimeframe = feedSettings.homeTimeframe
        )
    val viewModel: HomeViewModel = koinViewModel(key = HOME) {
        parametersOf(defaultEntity)
    }
    var viewEntity by remember(HOME) { mutableStateOf(defaultEntity) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val params by viewModel.params.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val topBar = @Composable {
        TopBar(
            title,
            params,
            slug = HOME,
            disableInfo = true,
            updateSort = viewModel::updateSort,
            updateView = { viewEntity = viewEntity.copy(view = it) },
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            openDrawer = { scope.launch { drawerState.open() } },
            entity = viewEntity
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
        viewEntity = viewEntity,
    )

}

@KoinViewModel
class HomeViewModel(
    repository: HomeRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditDao,
    @InjectedParam viewEntity: CommunityViewEntity,
) : PostFeedViewModel<SubredditData>(
    repository,
    visitedPostsDao,
    communityDao,
    viewEntity,
) {
    override suspend fun createViewEntity(name: String): CommunityViewEntity {
        return CommunityViewEntity(name = name, displayName = "Home")
    }
}


