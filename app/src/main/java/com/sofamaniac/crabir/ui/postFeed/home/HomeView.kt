package com.sofamaniac.crabir.ui.postFeed.home

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
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.HOME
import com.sofamaniac.crabir.ui.components.TabBar
import com.sofamaniac.crabir.ui.postFeed.FullFeedView
import com.sofamaniac.crabir.ui.postFeed.components.TopBar
import com.sofamaniac.crabir.ui.postFeed.defaultCommunityEntity
import com.sofamaniac.crabir.ui.postFeed.getCommunitySort
import com.sofamaniac.crabir.ui.postFeed.getCommunityView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeViewer(
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.Home)
    val initialSort = getCommunitySort(HOME)
    val viewModel: HomeViewModel = koinViewModel(key = HOME) {
        parametersOf(initialSort.sort, initialSort.timeframe)
    }

    val defaultView = getCommunityView(HOME)
    var viewEntity by remember(HOME) { mutableStateOf(defaultView) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val params by viewModel.params.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val topBar = @Composable {
        TopBar(
            title,
            params,
            slug = HOME,
            currentView = viewEntity,
            updateSort = viewModel::updateSort,
            updateView = { viewEntity = it },
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            openDrawer = { scope.launch { drawerState.open() } },
            defaultEntity = defaultCommunityEntity(HOME, title)
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

