package com.sofamaniac.crabir.ui.user

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.votable.CommentType
import com.sofamaniac.crabir.domain.model.votable.VotableData
import com.sofamaniac.crabir.domain.model.votable.post.PostData
import com.sofamaniac.crabir.map
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.PostRoute
import com.sofamaniac.crabir.ui.components.TabBar
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewer
import com.sofamaniac.crabir.ui.postFeed.PostView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun ProfileView(
    user: String?,
    modifier: Modifier = Modifier,
    initialTab: ProfileTabs = ProfileTabs.Overview,
    profileViewModel: ProfileViewModel = koinViewModel<ProfileViewModel> {
        parametersOf(user)
    },
) {
    val currentUser = LocalRedditAccount.current
    val isConnectedUser =
        currentUser.info?.username.map(default = { true }, map = { it == user })
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val tabs = if (isConnectedUser) ProfileTabs.entries else ProfileTabs.publicTabs
    val initialIndex = tabs.indexOf(initialTab).coerceIn(0, tabs.size)
    val currentTab = rememberPagerState(initialPage = initialIndex, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val userInfo by profileViewModel.userProfile.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = LocalNavController.current
    val onPostClick: (PostData) -> Unit = {
        navController?.navigate(PostRoute(it.permalink))
    }

    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(drawerState)
            },
        ) {
            val user = userInfo?.username ?: user
            if (user == null) return@ModalNavigationDrawer
            val viewModels: Map<ProfileTabs, ProfileFeedViewModel<out VotableData>> = mapOf(
                ProfileTabs.Overview to koinViewModel<OverviewViewModel>(key = user + "_overview") {
                    parametersOf(
                        user
                    )
                },
                ProfileTabs.Saved to koinViewModel<SavedViewModel>(key = user + "_saved") {
                    parametersOf(
                        user
                    )
                },
                ProfileTabs.Comments to koinViewModel<CommentsViewModel>(key = user + "_comments") {
                    parametersOf(
                        user
                    )
                },
                ProfileTabs.Upvoted to koinViewModel<UpvotedViewModel>(key = user + "_upvoted") {
                    parametersOf(
                        user
                    )
                },
                ProfileTabs.Downvoted to koinViewModel<DownvotedViewModel>(key = user + "_downvoted") {
                    parametersOf(
                        user
                    )
                },
                ProfileTabs.Hidden to koinViewModel<HiddenViewModel>(key = user + "_hidden") {
                    parametersOf(
                        user
                    )
                },
                ProfileTabs.Posts to koinViewModel<SubmittedViewModel>(key = user + "_submitted") {
                    parametersOf(
                        user
                    )
                },
            )


            val activeViewModel = tabs.getOrNull(currentTab.currentPage).let {
                viewModels.getOrDefault(it, defaultValue = null)
            }
            Scaffold(
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                },
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopBar(
                        scrollBehavior,
                        user ?: "",
                        userInfo,
                        viewModel = activeViewModel,
                        openDrawer = { scope.launch { drawerState.open() } }
                    )

                },
                bottomBar = {
                    TabBar(selected = 4)
                }
            ) { innerPadding ->
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    SecondaryScrollableTabRow(
                        containerColor = LocalTheme.current.toolbarBackground,
                        selectedTabIndex = currentTab.currentPage.coerceAtMost(tabs.size - 1),
                        modifier = Modifier.fillMaxWidth(),
                        edgePadding = 0.dp
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(selected = index == currentTab.currentPage, onClick = {
                                scope.launch { currentTab.animateScrollToPage(index) }
                            }, text = { Text(tab.stringResource()) })
                        }
                    }
                    HorizontalPager(
                        state = currentTab, modifier = Modifier.fillMaxSize()
                    ) {
                        val page = tabs[it]
                        when (val viewModel = viewModels[page]) {
                            null -> {
                                AboutTab(
                                    userInfo,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }

                            is SavedViewModel -> {
                                val filter by viewModel.filters.collectAsState()
                                TabInner(
                                    viewModel,
                                    filter = filter,
                                    showHidden = false,
                                    onPostClick = onPostClick
                                )
                            }

                            else -> {
                                TabInner(
                                    viewModel,
                                    filter = { true },
                                    showHidden = page == ProfileTabs.Hidden,
                                    onPostClick = onPostClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun TabInner(
    viewModel: ProfileFeedViewModel<out VotableData>,
    showHidden: Boolean,
    filter: (VotableData) -> Boolean,
    onPostClick: (PostData) -> Unit = {},
) {

    PostFeedViewer(
        viewModel = viewModel,
        viewEntity = null,
        filter = filter,
    ) { thing, isMostVisible ->
        when (thing) {
            is PostData -> PostView(
                thing,
                isMostVisible = isMostVisible,
                showHidden = showHidden,
                onClick = { onPostClick(thing) }
            )

            is CommentType.Comment -> CommentView(thing)

            else -> {
                LaunchedEffect(thing) {
                    Log.e("ProfileView", "Unknown thing: $thing")
                }
            }
        }
    }
}
