package com.sofamaniac.crabir.ui.postFeed

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.ui.components.ThemedScaffold
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.postFeed.components.BottomSheet
import com.sofamaniac.crabir.ui.postFeed.components.Fab
import com.sofamaniac.crabir.ui.thread.ThreadView
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


sealed interface FeedViewRoute : Parcelable {
    @Serializable
    @Parcelize
    object Feed : FeedViewRoute

    @Serializable
    @Parcelize
    class Post(val permalink: String) : FeedViewRoute
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3AdaptiveApi::class
)
@Composable
fun FullFeedView(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: FeedViewModelInterface<PostData>,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    viewEntity: ViewFull? = null,
    createPost: (Kind) -> Unit = {},
    filter: (PostData) -> Boolean = rememberPostsFilter(),
    feedInfo: (@Composable () -> Unit)? = null,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val theme = LocalTheme.current
    val viewSettings = LocalViewSettings.current

    LaunchedEffect(filter) {
        viewModel.updateFilters(filter)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(drawerState, snackbarHostState = snackbarHostState)
        },
    ) {
        val navigator = rememberListDetailPaneScaffoldNavigator<FeedViewRoute>()
        Surface(color = theme.cardBackground) {
            NavigableListDetailPaneScaffold(
                navigator = navigator,
                listPane = {
                    val route = navigator.currentDestination?.contentKey
                    val showFeed = (route !is FeedViewRoute.Post || viewSettings.splitScreenEnabled)
                    if (showFeed) {
                        FeedView(
                            snackbarHostState,
                            topBar,
                            bottomBar,
                            viewModel,
                            modifier,
                            viewEntity,
                            createPost,
                            filter,
                            feedInfo,
                            navigate = { route ->
                                scope.launch {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, route)
                                }
                            }
                        )
                    }
                },
                detailPane = {
                    val route = navigator.currentDestination?.contentKey
                    if (route !is FeedViewRoute.Post) {
                        if (viewSettings.splitScreenEnabled) {
                            SplitScreenPlaceHolder()
                        }
                    } else {
                        ThreadView(permalink = route.permalink, dismiss = {
                            scope.launch {
                                navigator.navigateBack()
                            }
                        })
                    }
                }
            )
        }
    }
}

@Composable
private fun SplitScreenPlaceHolder() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            stringResource(R.string.split_screen_placeholder),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedView(
    snackbarHostState: SnackbarHostState,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: FeedViewModelInterface<PostData>,
    modifier: Modifier = Modifier,
    viewEntity: ViewFull? = null,
    createPost: (Kind) -> Unit = {},
    filter: (PostData) -> Boolean = rememberPostsFilter(),
    feedInfo: (@Composable () -> Unit)? = null,
    navigate: (FeedViewRoute) -> Unit = {},
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
    val scope = rememberCoroutineScope()

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
        PostFeedViewer(
            viewModel,
            viewEntity = viewEntity,
            feedInfo = feedInfo,
            filter = filter,
            modifier = Modifier.padding(innerPadding)
        ) { post, isMosVisible ->
            PostView(
                post,
                isMostVisible = isMosVisible,
                showHidden = false,
                view = viewEntity?.view,
                onClick = {
                    navigate(FeedViewRoute.Post(post.permalink))
                }
            )
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
                createPost = createPost,
                cancel = {
                    scope.launch { bottomSheetState.hide() }
                        .invokeOnCompletion { showBottomSheet = false }
                }
            )
        }
    }
}



