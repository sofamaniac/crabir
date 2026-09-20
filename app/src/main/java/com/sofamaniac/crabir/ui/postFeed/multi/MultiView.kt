package com.sofamaniac.crabir.ui.postFeed.multi

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
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.MultiInfoRoute
import com.sofamaniac.crabir.navigation.routes.PostCreatorRoute
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
fun MultiView(
    slug: String,
    modifier: Modifier = Modifier,
) {
    val sort = getCommunitySort(slug)
    val viewModel: MultiViewModel = koinViewModel<MultiViewModel>(key = slug) {
        parametersOf(
            slug,
            sort.sort,
            sort.timeframe
        )
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val params by viewModel.params.collectAsState()
    val info by viewModel.info.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val defaultView = getCommunityView(slug)
    var fullView by remember { mutableStateOf(defaultView) }

    val defaultEntity = defaultCommunityEntity(slug, info?.displayNamePrefixed ?: slug)

    val navController = LocalNavController.current
    val topBar = @Composable {
        TopBar(
            info?.displayName ?: slug,
            params,
            info?.displayNamePrefixed ?: slug,
            updateSort = viewModel::updateSort,
            updateView = { fullView = fullView.copy(view = it) },
            refresh = viewModel::refresh,
            scrollBehavior = scrollBehavior,
            defaultEntity = defaultEntity,
            onInfoClick = {
                navController?.navigate(
                    MultiInfoRoute(
                        info?.displayNamePrefixed ?: slug
                    )
                )
            },
            openDrawer = { scope.launch { drawerState.open() } }
        )
    }
    val bottomBar = @Composable {
        TabBar(
            2,
            onTabReselect = {
                scope.launch {
                    viewModel.listState.animateScrollToItem(0)
                }
            }
        )
    }
    FullFeedView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        drawerState = drawerState,
        viewEntity = fullView,
        createPost = { kind -> navController?.navigate(PostCreatorRoute(kind, null)) }
    )
}

