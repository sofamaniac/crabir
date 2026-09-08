package com.sofamaniac.crabir.ui.user

import androidx.annotation.Keep
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.ui.components.TabBar
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewer
import com.sofamaniac.crabir.ui.postFeed.PostView
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Serializable
@Keep
enum class ProfileTabs {
    Overview, About, Posts, Comments, Saved, Upvoted, Downvoted, Hidden;

    @Composable
    fun stringResource(): String {
        return stringResource(
            when (this) {
                Overview -> R.string.profile_tab_overview
                About -> R.string.profile_tab_about
                Posts -> R.string.profile_tab_submitted
                Comments -> R.string.profile_tab_comments
                Saved -> R.string.profile_tab_saved
                Upvoted -> R.string.profile_tab_upvoted
                Downvoted -> R.string.profile_tab_downvoted
                Hidden -> R.string.profile_tab_hidden
            }
        )
    }

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
    profileViewModel: ProfileViewModel = koinViewModel<ProfileViewModel> {
        parametersOf(user)
    },
) {
    //    val isConnectedUser by remember { profileViewModel.currentUser.map { it == user } }.collectAsState(
    //        true
    //    )
    val currentUser = LocalRedditAccount.current
    val isConnectedUser = currentUser.info?.username == user
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val tabs = if (isConnectedUser) ProfileTabs.entries else ProfileTabs.publicTabs
    val initialIndex = tabs.indexOf(initialTab).coerceIn(0, tabs.size)
    val currentTab = rememberPagerState(initialPage = initialIndex, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val viewModels: Map<ProfileTabs, ProfileFeedViewModel<out VotableData>> = mapOf(
        ProfileTabs.Overview to koinViewModel<OverviewViewModel> { parametersOf(user) },
        ProfileTabs.Saved to koinViewModel<SavedViewModel> { parametersOf(user) },
        ProfileTabs.Comments to koinViewModel<CommentsViewModel> { parametersOf(user) },
        ProfileTabs.Upvoted to koinViewModel<UpvotedViewModel> { parametersOf(user) },
        ProfileTabs.Downvoted to koinViewModel<DownvotedViewModel> { parametersOf(user) },
        ProfileTabs.Hidden to koinViewModel<HiddenViewModel> { parametersOf(user) },
        ProfileTabs.Posts to koinViewModel<SubmittedViewModel> { parametersOf(user) },
    )


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val activeViewModel = tabs.getOrNull(currentTab.currentPage).let {
        viewModels.getOrDefault(it, defaultValue = null)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(drawerState)
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
                        viewModel = activeViewModel,
                        openDrawer = { scope.launch { drawerState.open() } }
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
                            }, text = { Text(tab.stringResource()) })
                        }
                    }
                    HorizontalPager(
                        state = currentTab, modifier = Modifier.fillMaxSize()
                    ) {
                        val page = tabs[it]
                        val viewModel = viewModels[page]
                        if (viewModel != null) {
                            PostFeedViewer(
                                viewModel = viewModel,
                                viewEntity = null,
                                filter = { true }
                            ) { thing, isMostVisible ->
                                when (thing) {
                                    is PostData -> PostView(
                                        thing,
                                        isMostVisible = isMostVisible,
                                        showHidden = page == ProfileTabs.Hidden,
                                    )

                                    is CommentType.Comment -> CommentView(thing)

                                    else -> Text("Unknown type ${thing::class}")
                                }
                            }
                        } else {
                            AboutTab(
                                profileViewModel.userProfile.value,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}
