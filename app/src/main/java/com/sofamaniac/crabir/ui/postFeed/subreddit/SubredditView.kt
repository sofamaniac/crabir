package com.sofamaniac.crabir.ui.postFeed.subreddit

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.settings.views.viewSettingDataStore
import com.sofamaniac.crabir.ui.components.TabBar
import com.sofamaniac.crabir.ui.postFeed.FullFeedView
import com.sofamaniac.crabir.ui.postFeed.components.TopBar
import com.sofamaniac.crabir.ui.postFeed.getCommunityViewEntity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubredditViewer(
    subreddit: String,
    modifier: Modifier = Modifier.Companion,
) {
    val defaultEntity = getCommunityViewEntity(subreddit, subreddit)
    val subredditName = subreddit.split("/").last()
    val viewModel: SubredditViewModel =
        koinViewModel(key = subreddit) {
            parametersOf(
                subreddit,
                defaultEntity,
            )
        }
    var entity by remember(subreddit) { mutableStateOf(defaultEntity) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val params by viewModel.params.collectAsState()
    val feedInfo by viewModel.info.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val viewDataStore = LocalContext.current.viewSettingDataStore
    LaunchedEffect(feedInfo) {
        if (feedInfo == null) return@LaunchedEffect
        scope.launch {
            viewDataStore.updateData {
                it.copy(
                    rememberedViews = it.rememberedViews + (subreddit to entity.copy(
                        displayName = feedInfo!!.displayNamePrefixed
                    ))
                )
            }
        }
    }

    val topBar = @Composable {
        TopBar(
            feedInfo?.displayName ?: subreddit,
            params,
            slug = subreddit,
            updateSort = viewModel::updateSort,
            updateView = { entity = entity.copy(view = it) },
            refresh = viewModel::refresh,
            entity = entity,
            scrollBehavior = scrollBehavior,
            openDrawer = { scope.launch { drawerState.open() } }
        )
    }
    val bottomBar = @Composable {
        TabBar(2)
    }
    val feedInfoView = feedInfo?.let { info ->
        @Composable {
            SubredditInfo(info, viewModel)
        }
    }

    FullFeedView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        filter = rememberPostsFilter(whitelistSubreddit = listOf(subredditName)),
        drawerState = drawerState,
        feedInfo = feedInfoView,
        viewEntity = entity,
    )
}

