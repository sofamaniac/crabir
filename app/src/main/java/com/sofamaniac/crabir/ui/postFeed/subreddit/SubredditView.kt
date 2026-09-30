package com.sofamaniac.crabir.ui.postFeed.subreddit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.window.core.layout.WindowSizeClass
import com.sofamaniac.crabir.LocalPostSettings
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.LocalViewSettings
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
import com.sofamaniac.crabir.ui.postFeed.FeedViewRoute
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewer
import com.sofamaniac.crabir.ui.postFeed.PostView
import com.sofamaniac.crabir.ui.postFeed.SortFull
import com.sofamaniac.crabir.ui.postFeed.ViewFull
import com.sofamaniac.crabir.ui.postFeed.components.BottomSheet
import com.sofamaniac.crabir.ui.postFeed.components.Fab
import com.sofamaniac.crabir.ui.postFeed.components.TopBar
import com.sofamaniac.crabir.ui.postFeed.defaultCommunityEntity
import com.sofamaniac.crabir.ui.postFeed.getCommunitySort
import com.sofamaniac.crabir.ui.postFeed.getCommunityView
import com.sofamaniac.crabir.ui.thread.ThreadView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SubredditViewer(
    subreddit: String,
    modifier: Modifier = Modifier,
    tabSelected: Int = 2,
) {

    val initialSort: SortFull = getCommunitySort(subreddit)
    val viewModel: SubredditViewModel =
        koinViewModel(key = subreddit) {
            parametersOf(
                subreddit,
                initialSort.sort,
                initialSort.timeframe,
            )
        }
    SubredditViewer(subreddit, modifier, viewModel = viewModel, tabSelected = tabSelected)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun SubredditViewer(
    subreddit: String,
    modifier: Modifier = Modifier,
    viewModel: SubredditViewModel,
    tabSelected: Int,
) {
    val subredditName = subreddit.split("/").last()
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
            currentView = fullView,
            updateSort = viewModel::updateSort,
            updateView = { fullView = it },
            refresh = viewModel::refresh,
            defaultEntity = defaultCommunityEntity(subreddit, feedInfo?.displayName ?: subreddit),
            scrollBehavior = scrollBehavior,
            openDrawer = { scope.launch { drawerState.open() } }
        )
    }
    val bottomBar = @Composable {
        TabBar(tabSelected)
    }
    Surface(color = LocalTheme.current.background) {
        SubredditScaffold(
            topBar, bottomBar, viewModel,
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            filter = filters,
            drawerState = drawerState,
            feedInfo = feedInfo,
            viewEntity = fullView,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun SubredditScaffold(
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
    val viewSettings = LocalViewSettings.current

    val navigator = rememberListDetailPaneScaffoldNavigator<FeedViewRoute>()
    val isLarge = currentWindowAdaptiveInfo().windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val enableDrawer = navigator.currentDestination?.contentKey !is FeedViewRoute.Post
    val infoSettings = LocalPostSettings.current.infoSettings.copy(clickableCommunity = false)
    CompositionLocalProvider(LocalPostSettings provides LocalPostSettings.current.copy(infoSettings = infoSettings)) {
        ModalNavigationDrawer(
            gesturesEnabled = enableDrawer,
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(drawerState, snackbarHostState = snackbarHostState)
            },
        ) {
            NavigableListDetailPaneScaffold(
                navigator = navigator,
                paneExpansionDragHandle = { state ->
                    val interactionSource = remember { MutableInteractionSource() }
                    VerticalDragHandle(
                        interactionSource = interactionSource,
                        modifier = Modifier.paneExpansionDraggable(
                            state,
                            LocalMinimumInteractiveComponentSize.current,
                            interactionSource,
                        ),
                    )
                },
                listPane = {
                    AnimatedPane {
                        val route = navigator.currentDestination?.contentKey
                        val showFeed = (route == null || viewSettings.splitScreenEnabled)
                        if (showFeed) {
                            SubredditListPane(
                                isLarge = isLarge && viewSettings.splitScreenEnabled,
                                topBar = topBar,
                                bottomBar = bottomBar,
                                viewModel = viewModel,
                                modifier = modifier,
                                snackbarHostState = snackbarHostState,
                                viewEntity = viewEntity,
                                filter = filter,
                                feedInfo = feedInfo,
                                showBottomSheet = {
                                    scope.launch {
                                        bottomSheetState.show()
                                        showBottomSheet = true
                                    }
                                },
                                navigate = {
                                    scope.launch {
                                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                                    }
                                }
                            )
                        }
                    }
                },
                detailPane = {
                    AnimatedPane {
                        val route = navigator.currentDestination?.contentKey
                        if (route is FeedViewRoute.Post) {
                            ThreadView(permalink = route.permalink, dismiss = {
                                scope.launch {
                                    navigator.navigateBack()
                                }
                            })
                        } else if (viewSettings.splitScreenEnabled && isLarge && feedInfo != null) {
                            Scaffold { padding ->
                                SubredditInfoView(
                                    feedInfo,
                                    viewModel,
                                    modifier = modifier.padding(padding)
                                )
                            }
                        }
                    }
                }
            )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubredditListPane(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: SubredditViewModel,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    isLarge: Boolean,
    viewEntity: ViewFull? = null,
    filter: (PostData) -> Boolean = rememberPostsFilter(),
    navigate: (FeedViewRoute) -> Unit = {},
    showBottomSheet: () -> Unit,
    feedInfo: SubredditData? = null,
) {

    val feedInfoView = feedInfo?.let { info ->
        @Composable {
            SubredditInfo(info, viewModel)
        }
    }
    ThemedScaffold(
        snackbarHostState = snackbarHostState,
        topBar = topBar,
        bottomBar = bottomBar,
        modifier = modifier,
        floatingActionButton = {
            Fab(viewModel, toggleBottomSheet = {
                showBottomSheet()
            })
        }
    ) { innerPadding ->
        if (!isLarge) {
            TabbedView(
                viewModel,
                viewEntity,
                filter,
                feedInfo,
                feedInfoView,
                navigate,
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            PostFeedViewer(viewModel, viewEntity, filter, feedInfoView, navigate)
        }
    }
}

@Composable
private fun TabbedView(
    viewModel: SubredditViewModel,
    viewEntity: ViewFull? = null,
    filter: (PostData) -> Boolean = rememberPostsFilter(),
    feedInfo: SubredditData? = null,
    feedInfoView: @Composable (() -> Unit)? = null,
    navigate: (FeedViewRoute) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier,
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
                    viewEntity,
                    filter,
                    feedInfoView,
                    navigate
                )
            } else {
                SubredditInfoView(feedInfo, viewModel)
            }
        }
    }
}


@Composable
private fun PostFeedViewer(
    viewModel: SubredditViewModel,
    viewEntity: ViewFull?,
    filter: (PostData) -> Boolean,
    feedInfoView: @Composable (() -> Unit)? = null,
    navigate: (FeedViewRoute) -> Unit = {},
) {
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
            onClick = { navigate(FeedViewRoute.Post(post.permalink)) }
        )
    }
}

@Composable
private fun SubredditInfoView(
    feedInfo: SubredditData?,
    viewModel: SubredditViewModel,
    modifier: Modifier = Modifier,
) {
    if (feedInfo == null) return
    SubredditInfoView(
        modifier = modifier,
        info = feedInfo,
        subscribe = { subscribed ->
            if (subscribed) {
                viewModel.subscribe()
            } else {
                viewModel.unsubscribe()
            }
        },
        favorite = { favorited ->
            viewModel.favorite(favorited)
        }
    )
}
