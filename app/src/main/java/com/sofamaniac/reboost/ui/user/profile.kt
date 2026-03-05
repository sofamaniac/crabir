package com.sofamaniac.reboost.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.sofamaniac.reboost.FullscreenHandler
import com.sofamaniac.reboost.LocalDrawerState
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.ui.TabBar
import com.sofamaniac.reboost.ui.subreddit.PostFeedViewer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    showSettings: Boolean = true,
    showSort: Boolean = true
) {
    val scope = rememberCoroutineScope()
    TopAppBar(title = {}, navigationIcon = {
        IconButton(onClick = { scope.launch { drawerState.open() } }) {
            Icon(Icons.Default.Menu, "Open Drawer")
        }
    }, actions = {})
}

enum class ProfileTabs {
    Overview, About, Posts, Comments, Saved, Upvoted, Downvoted, Hidden;

    companion object {
        val publicTabs get() = listOf(Overview, About, Posts, Comments)
    }
}

@Composable
fun ProfileInfo(modifier: Modifier = Modifier) {
    Text("WIP")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    user: String,
    selected: State<Int>,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val isConnectedUser by viewModel.currentUser.map { it == user }.collectAsState(true)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val tabs = if (isConnectedUser) ProfileTabs.entries else ProfileTabs.publicTabs
    val currentTab = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    val viewModels: Map<ProfileTabs, ProfileFeedViewModel> = mapOf(
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

    FullscreenHandler {
        Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            TopAppBar(scrollBehavior = scrollBehavior, title = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(user)
                }

            }, navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        Icons.Default.Menu, "Open Drawer"
                    )
                }
            }, actions = {})
        }, bottomBar = {
            TabBar(selected = selected)
        }) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Top, modifier = Modifier.padding(innerPadding)
            ) {
                SecondaryScrollableTabRow(
                    selectedTabIndex = currentTab.currentPage,
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
                    if (viewModel != null) {
                        PostFeedViewer(state = viewModel)
                    } else {
                        Text("TODO")
                    }
                }
            }
        }
    }
}

@HiltViewModel
class ProfileViewModel @Inject constructor(accountsRepository: AccountsRepository) :
    ViewModel() {
    val currentUser = accountsRepository.activeAccount.map { it.username }
}