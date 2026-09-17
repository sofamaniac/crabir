package com.sofamaniac.crabir.ui.postFeed.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.PostCreatorRoute
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.settings.views.viewSettingDataStore
import com.sofamaniac.crabir.ui.components.TabBar
import com.sofamaniac.crabir.ui.components.ThemedScaffold
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.feedInfo.subreddit.SubredditInfoView
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewer
import com.sofamaniac.crabir.ui.postFeed.PostView
import com.sofamaniac.crabir.ui.postFeed.components.BottomSheet
import com.sofamaniac.crabir.ui.postFeed.components.Fab
import com.sofamaniac.crabir.ui.postFeed.components.TopBar
import com.sofamaniac.crabir.ui.postFeed.getCommunityViewEntity
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubredditViewer(
    subreddit: String,
    modifier: Modifier = Modifier,
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
    val navController = LocalNavController.current

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
            //            onInfoClick = {
            //                navController?.navigate(
            //                    SubredditInfoRoute(
            //                        feedInfo?.displayNamePrefixed ?: subreddit
            //                    )
            //                )
            //            },
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
        viewEntity = entity,
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
    viewEntity: CommunityViewEntity? = null,
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
            Column(modifier = modifier.padding(innerPadding)) {
                SecondaryTabRow(
                    containerColor = LocalTheme.current.cardBackground,
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 50.dp),
                ) {
                    Tab(
                        modifier = Modifier.background(LocalTheme.current.cardBackground),
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
                                viewEntity?.name
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
