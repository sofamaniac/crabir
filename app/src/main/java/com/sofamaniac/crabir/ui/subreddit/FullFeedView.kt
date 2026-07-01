package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostCreatorRoute
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FullFeedView(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: FeedViewModelInterface<PostData>,
    modifier: Modifier = Modifier,
    filter: (PostData) -> Boolean = rememberPostsFilter(),
    feedInfo: (@Composable () -> Unit)? = null,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val communityEntity by viewModel.entity.collectAsState(initial = null)
    val navController = LocalNavController.current
    val drawerState = LocalDrawerState.current
    val currentAccount = LocalRedditAccount.current
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent()
            },
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                },
                topBar = topBar,
                bottomBar = bottomBar,
                modifier = modifier,
                floatingActionButton = {
                    Fab(viewModel, toggleBottomSheet = {
                        scope.launch {
                            bottomSheetState.show()
                        }.invokeOnCompletion {
                            showBottomSheet = !showBottomSheet
                        }
                    })
                }
            ) { innerPadding ->
                PostFeedViewer(
                    viewModel,
                    feedInfo = feedInfo,
                    filter = filter,
                    modifier = Modifier.padding(innerPadding)
                ) { post, isMosVisible ->
                    PostView(
                        post,
                        isMostVisible = isMosVisible,
                        read = viewModel.isPostRead(post),
                        markAsRead = {
                            viewModel.visitPost(post, currentAccount.id)
                        },
                        showHidden = false,
                        view = communityEntity?.view,
                    )
                }

                if (showBottomSheet) {
                    FeedBottomSheet(
                        bottomSheetState,
                        onDismiss = {
                            showBottomSheet = false
                        },
                        createPost = { kind ->
                            navController?.navigate(
                                PostCreatorRoute(
                                    kind,
                                    communityEntity?.name
                                )
                            )
                        },
                        cancel = {
                            scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                                if (!bottomSheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}




