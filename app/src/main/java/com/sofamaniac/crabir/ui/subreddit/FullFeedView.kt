package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.animateFloatingActionButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostCreatorRoute
import com.sofamaniac.crabir.settings.filters.rememberPostsFilter
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import kotlinx.coroutines.launch


data class PostType(val name: String, val kind: Kind, val icon: ImageVector)

val postTypes = listOf(
    PostType("Text", Kind.Self, Icons.AutoMirrored.Filled.Article),
    PostType("Link", Kind.Link, Icons.Default.Link),
    PostType("Image", Kind.Image, Icons.Default.Image),
    PostType("Video", Kind.Video, Icons.Default.VideoFile)
)

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
    val navController = LocalNavController.current!!
    val drawerState = LocalDrawerState.current
    val currentAccount = LocalRedditAccount.current
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent()
        },
        //gesturesEnabled = enabledDrawer
    ) {
        Scaffold(
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
                DefaultPostView(
                    post,
                    isMostVisible = isMosVisible,
                    read = viewModel.isPostRead(post),
                    markAsRead = {
                        viewModel.visitPost(post, currentAccount.id)
                    },
                    showHidden = false
                )
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = bottomSheetState,
                ) {
                    ListItem(headlineContent = { Text(stringResource(R.string.create_post)) })
                    for (type in postTypes) {
                        ListItem(
                            headlineContent = { Text(type.name) },
                            leadingContent = { Icon(type.icon, contentDescription = null) },
                            modifier = Modifier.clickable {
                                //createPost(type.kind)
                                navController.navigate(
                                    PostCreatorRoute(
                                        type.kind,
                                        communityEntity?.name
                                    )
                                )
                            }
                        )
                    }
                    TextButton(onClick = {
                        scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                            if (!bottomSheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Fab(viewModel: FeedViewModelInterface<PostData>, toggleBottomSheet: () -> Unit) {
    val scope = rememberCoroutineScope()
    var expandFab by remember { mutableStateOf(false) }
    val showFab by remember {
        derivedStateOf {
            !viewModel.listState.canScrollBackward
                    || !viewModel.listState.canScrollForward
                    || viewModel.listState.lastScrolledBackward
        }
    }
    val theme = LocalTheme.current
    FloatingActionButtonMenu(
        expanded = expandFab,
        button = {
            ToggleFloatingActionButton(
                containerColor = { theme.highlight },
                modifier = Modifier
                    .semantics {
                        stateDescription =
                            if (expandFab) "Expanded" else "Collapsed"
                        contentDescription = "Toggle menu"
                    }
                    .animateFloatingActionButton(
                        visible = showFab || expandFab,
                        alignment = Alignment.BottomEnd
                    ),
                checked = expandFab,
                onCheckedChange = { expandFab = !expandFab },
            ) {

                val icon by remember {
                    derivedStateOf {
                        if (checkedProgress > 0.5f) Icons.Default.Close else Icons.Default.Add
                    }
                }
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.animateIcon({ checkedProgress })
                )
            }
        }
    ) {
        FloatingActionButtonMenuItem(
            onClick = { scope.launch { viewModel.listState.scrollToItem(0) } },
            containerColor = theme.highlight,
            icon = {
                Icon(
                    Icons.Default.KeyboardDoubleArrowUp,
                    contentDescription = null
                )
            },
            text = { Text(stringResource(R.string.go_to_top)) }
        )
        FloatingActionButtonMenuItem(
            onClick = { toggleBottomSheet() },
            containerColor = theme.highlight,
            icon = {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = null
                )
            },
            text = { Text(stringResource(R.string.create_post)) }
        )
    }
}