package com.sofamaniac.crabir.ui.user

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.ui.TabBar
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.subreddit.PostFeedViewer
import com.sofamaniac.crabir.ui.subreddit.PostView
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
@Keep
enum class ProfileTabs {
    Overview, About, Posts, Comments, Saved, Upvoted, Downvoted, Hidden;

    companion object {
        val publicTabs get() = listOf(Overview, About, Posts, Comments)
        fun fromString(string: String): ProfileTabs {
            return when (string.lowercase()) {
                "about" -> About
                "submitted" -> Posts
                "comments" -> Comments
                "saved" -> Saved
                "upvoted" -> Upvoted
                "downvoted" -> Downvoted
                "hidden" -> Hidden
                else -> Overview
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    user: String,
    modifier: Modifier = Modifier,
    initialTab: ProfileTabs = ProfileTabs.Overview,
    profileViewModel: ProfileViewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory> { factory ->
        factory.create(user)
    },
) {
    val isConnectedUser by remember { profileViewModel.currentUser.map { it == user } }.collectAsState(
        true
    )
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val tabs = if (isConnectedUser) ProfileTabs.entries else ProfileTabs.publicTabs
    val initialIndex = tabs.indexOf(initialTab).coerceIn(0, tabs.size)
    val currentTab = rememberPagerState(initialPage = initialIndex, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val viewModels: Map<ProfileTabs, ProfileFeedViewModel<out VotableData>> = mapOf(
        ProfileTabs.Overview to hiltViewModel<OverviewViewModel, OverviewViewModel.Factory> { factory ->
            factory.create(
                user
            )
        },
        ProfileTabs.Saved to hiltViewModel<SavedViewModel, SavedViewModel.Factory> { factory ->
            factory.create(
                user
            )
        },
        ProfileTabs.Comments to hiltViewModel<CommentsViewModel, CommentsViewModel.Factory> { factory ->
            factory.create(
                user
            )
        },
        ProfileTabs.Upvoted to hiltViewModel<UpvotedViewModel, UpvotedViewModel.Factory> { factory ->
            factory.create(
                user
            )
        },
        ProfileTabs.Downvoted to hiltViewModel<DownvotedViewModel, DownvotedViewModel.Factory> { factory ->
            factory.create(
                user
            )
        },
        ProfileTabs.Hidden to hiltViewModel<HiddenViewModel, HiddenViewModel.Factory> { factory ->
            factory.create(
                user
            )
        },
        ProfileTabs.Posts to hiltViewModel<SubmittedViewModel, SubmittedViewModel.Factory> { factory ->
            factory.create(
                user
            )
        }
    )


    val drawerState = LocalDrawerState.current
    val activeViewModel = tabs.getOrNull(currentTab.currentPage).let {
        viewModels.getOrDefault(it, defaultValue = null)
    }
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
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopBar(
                        scrollBehavior,
                        user,
                        profileViewModel.userProfile.value,
                        viewModel = activeViewModel
                    )

                },
                bottomBar = {
                    TabBar(selected = 4)
                }) { innerPadding ->
                Column(
                    verticalArrangement = Arrangement.Top, modifier = Modifier.padding(innerPadding)
                ) {
                    SecondaryScrollableTabRow(
                        selectedTabIndex = currentTab.currentPage.coerceAtMost(tabs.size - 1),
                        modifier = Modifier.fillMaxWidth(),
                        edgePadding = 0.dp
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            Tab(selected = index == currentTab.currentPage, onClick = {
                                scope.launch { currentTab.animateScrollToPage(index) }
                            }, text = { Text(tab.name) })
                        }
                    }
                    HorizontalPager(
                        state = currentTab, modifier = Modifier.fillMaxSize()
                    ) {
                        val page = tabs[it]
                        val viewModel = viewModels[page]
                        val currentAccount = LocalRedditAccount.current
                        if (viewModel != null) {
                            PostFeedViewer(
                                viewModel = viewModel,
                                filter = { true }
                            ) { thing, isMostVisible ->
                                when (thing) {
                                    is PostData -> PostView(
                                        thing,
                                        isMostVisible = isMostVisible,
                                        markAsRead = {
                                            viewModel.visitPost(
                                                thing,
                                                currentAccount.id
                                            )
                                        },
                                        read = viewModel.isPostRead(thing),
                                        showHidden = page == ProfileTabs.Hidden,
                                    )

                                    is CommentData -> CommentView(
                                        thing,
                                    )
                                }
                            }
                        } else {
                            AboutTab(
                                profileViewModel.userProfile.value,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
