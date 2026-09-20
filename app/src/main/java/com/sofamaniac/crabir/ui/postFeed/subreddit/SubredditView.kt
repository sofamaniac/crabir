package com.sofamaniac.crabir.ui.postFeed.subreddit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.rememberBottomSheetState
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
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.PostCreatorRoute
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.ui.components.TabBar
import com.sofamaniac.crabir.ui.components.ThemedScaffold
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.feedInfo.subreddit.SubredditInfoView
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewer
import com.sofamaniac.crabir.ui.postFeed.PostView
import com.sofamaniac.crabir.ui.postFeed.ViewFull
import com.sofamaniac.crabir.ui.postFeed.components.BottomSheet
import com.sofamaniac.crabir.ui.postFeed.components.Fab
import com.sofamaniac.crabir.ui.postFeed.components.TopBar
import com.sofamaniac.crabir.ui.postFeed.defaultCommunityEntity
import com.sofamaniac.crabir.ui.postFeed.getCommunitySort
import com.sofamaniac.crabir.ui.postFeed.getCommunityView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubredditViewer(
    subreddit: String,
    modifier: Modifier = Modifier,
) {
    val initialSort = getCommunitySort(subreddit)
    val subredditName = subreddit.split("/").last()
    val viewModel: SubredditViewModel =
        koinViewModel(key = subreddit) {
            parametersOf(
                subreddit,
                initialSort.sort,
                initialSort.timeframe,
            )
        }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val params by viewModel.params.collectAsState()
    val feedInfo by viewModel.info.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val filters = rememberPostsFilter(whitelistSubreddit = listOf(subredditName))
    LaunchedEffect(filters) {
        viewModel.updateFilters(filters)
    }

    val defaultView = getCommunityView(subreddit)
    var fullView by remember { mutableStateOf(defaultView) }

    val topBar = @Composable {
        TopBar(
            feedInfo?.displayName ?: subreddit,
            params,
            slug = subreddit,
            updateSort = viewModel::updateSort,
            updateView = { fullView = fullView.copy(view = it) },
            refresh = viewModel::refresh,
            defaultEntity = defaultCommunityEntity(subreddit, feedInfo?.displayName ?: subreddit),
            scrollBehavior = scrollBehavior,
            openDrawer = { scope.launch { drawerState.open() } }
        )
    }
    val bottomBar = @Composable {
        TabBar(2)
    }
    InnerView(
        topBar, bottomBar, viewModel,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        filter = rememberPostsFilter(whitelistSubreddit = listOf(subredditName)),
        drawerState = drawerState,
        feedInfo = feedInfo,
        viewEntity = fullView,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InnerView(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: SubredditViewModel,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    viewEntity: ViewFull? = null,
    filter: (PostData) -> Boolean = rememberPostsFilter(),
    feedInfo: SubredditData? = null,
) {

    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }

    val pagerState = rememberPagerState(pageCount = { 2 })

    val feedInfoView = feedInfo?.let { info ->
        @Composable {
            SubredditInfo(info, viewModel)
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(drawerState, snackbarHostState = snackbarHostState)
        },
    ) {
        ThemedScaffold(
            snackbarHostState = snackbarHostState,
            topBar = topBar,
            bottomBar = bottomBar,
            modifier = modifier,
            floatingActionButton = {
                Fab(viewModel, toggleBottomSheet = {
                    scope.launch {
                        bottomSheetState.show()
                    }.invokeOnCompletion {
                        showBottomSheet = true
                    }
                })
            }
        ) { innerPadding ->
            Column(
                modifier = modifier.padding(innerPadding),
                verticalArrangement = Arrangement.Top
            ) {
                SecondaryTabRow(
                    containerColor = LocalTheme.current.toolbarBackground,
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        onClick = { scope.launch { pagerState.animateScrollToPage(0) } }
                    ) {
                        Text(stringResource(R.string.subreddit_posts_tab))
                    }
                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = { scope.launch { pagerState.animateScrollToPage(1) } }
                    ) {
                        Text(stringResource(R.string.subreddit_info_tab))
                    }
                }
                HorizontalPager(state = pagerState, userScrollEnabled = false) { index ->
                    if (index == 0) {
                        PostFeedViewer(
                            viewModel,
                            viewEntity = viewEntity,
                            feedInfo = feedInfoView,
                            filter = filter,
                        ) { post, isMosVisible ->
                            PostView(
                                post,
                                isMostVisible = isMosVisible,
                                showHidden = false,
                                view = viewEntity?.view,
                            )
                        }
                    } else {
                        if (feedInfo == null) return@HorizontalPager
                        SubredditInfoView(info = feedInfo, subscribe = { subscribed ->
                            if (subscribed) {
                                viewModel.subscribe()
                            } else {
                                viewModel.unsubscribe()
                            }
                        }, favorite = { favorited ->
                            viewModel.favorite(favorited)
                        })
                    }
                }
            }

            if (showBottomSheet) {
                BottomSheet(
                    bottomSheetState,
                    onDismiss = {
                        scope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                        }
                    },
                    createPost = { kind ->
                        navController?.navigate(
                            PostCreatorRoute(
                                kind,
                                feedInfo?.displayNamePrefixed
                            )
                        )
                    },
                    cancel = {
                        scope.launch { bottomSheetState.hide() }
                            .invokeOnCompletion { showBottomSheet = false }
                    }
                )
            }
        }
    }
}
