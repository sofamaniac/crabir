package com.sofamaniac.crabir.ui.postFeed

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.PostCreatorRoute
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.ui.components.ThemedScaffold
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.postFeed.components.BottomSheet
import com.sofamaniac.crabir.ui.postFeed.components.Fab
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FullFeedView(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: FeedViewModelInterface<PostData>,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    viewEntity: CommunityViewEntity? = null,
    filter: (PostData) -> Boolean = rememberPostsFilter(),
    feedInfo: (@Composable () -> Unit)? = null,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberBottomSheetState(initialValue = SheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(filter) {
        viewModel.updateFilters(filter)
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




