package com.sofamaniac.crabir.ui.user

import androidx.annotation.Keep
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.LocalDrawerState
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.TabBar
import com.sofamaniac.crabir.ui.drawer.DrawerContent
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.subreddit.DefaultPostView
import com.sofamaniac.crabir.ui.subreddit.PostFeedViewer
import com.sofamaniac.crabir.ui.thread.CommentViewModelInterface
import com.sofamaniac.crabir.ui.thread.OpenedComment
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Instant
import kotlin.time.toJavaInstant


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
    }
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
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent()
        },
    ) {
        Scaffold(
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
                        ) { thing, isMostVisible ->
                            when (thing) {
                                is PostData -> DefaultPostView(
                                    thing,
                                    isMostVisible = isMostVisible,
                                    markAsRead = { viewModel.visitPost(thing, currentAccount.id) },
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

@Composable
fun CommentView(
    thing: CommentData,
) {
    val navController = LocalNavController.current!!
    Column(
        modifier = Modifier.clickable {
            navController.navigate(
                PostRoute(
                    thing.permalink,
                    comment = thing.id,
                )
            )
        }
    ) {
        OpenedComment(
            thing,
            viewModel = hiltViewModel<CommentViewModel, CommentViewModel.Factory> { factory ->
                factory.create(thing)
            },
            enableAnimation = false,
        )
    }
}

@HiltViewModel(assistedFactory = CommentViewModel.Factory::class)
class CommentViewModel @AssistedInject constructor(
    @Assisted val comment: CommentData,
    private val commentsRepository: CommentsRepository,
) : CommentViewModelInterface, ViewModel() {
    override val openComment: StateFlow<Fullname?> = MutableStateFlow(comment.name)

    override fun submitComment(
        parent: Fullname,
        body: String
    ) {
        TODO("Not yet implemented")
    }

    override val likes: Flow<Boolean?> = flowOf(comment.relationship.liked)
    override val saved: Flow<Boolean> = flowOf(comment.relationship.saved)
    override val rules: StateFlow<Rules>
        get() = TODO("Not yet implemented")

    override fun upvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsRepository.upvote(name)
        }
    }

    override fun downvote(name: Fullname) {
        viewModelScope.launch(Dispatchers.IO) {
            commentsRepository.downvote(name)
        }
    }

    override fun save(name: Fullname, target: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target) {
                commentsRepository.unsave(name)
            } else {
                commentsRepository.save(name)
            }
        }
    }

    override fun fetchRules() {
        TODO("Not yet implemented")
    }

    override fun report(reason: String) {
        TODO("Not yet implemented")
    }

    @AssistedFactory
    interface Factory {
        fun create(comment: CommentData): CommentViewModel
    }

}

@Composable
fun AboutTab(user: UserDTO?, modifier: Modifier = Modifier) {
    if (user == null) return

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top) {
        Text(user.subreddit.publicDescription)
        Spacer(Modifier.height(8.dp))
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("KARMA")
                Text("${user.totalKarma}")
                Row {
                    Icon(Icons.Default.Link, contentDescription = "Link karma")
                    Text("${user.linkKarma}")
                    Icon(Icons.Outlined.ModeComment, contentDescription = "Comment karma")
                    Text("${user.commentKarma}")
                }
                Row {
                    Icon(Icons.Default.CardGiftcard, contentDescription = "Awarder karma")
                    Text("${user.awarderKarma}")
                    Icon(
                        Icons.AutoMirrored.Filled.CallReceived,
                        contentDescription = "Awardee karma"
                    )
                    Text("${user.awardeeKarma}")
                }
            }
            Column {
                val created = Instant.fromEpochSeconds(user.createdUtc.toLong())
                val formatter = DateTimeFormatter
                    .ofPattern("MMM dd, yyyy")
                    .withLocale(LocalLocale.current.platformLocale)
                    .withZone(ZoneId.systemDefault())

                Text("REDDIT AGE")
                Text(formatElapsedTimeLocalized(created))
                Row {
                    Icon(Icons.Default.Cake, contentDescription = null)
                    Text(formatter.format(created.toJavaInstant()))
                }
            }
        }
    }
}

@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
class ProfileViewModel @AssistedInject constructor(
    accountsRepository: AccountsRepository,
    api: RedditAPIService,
    @Assisted username: String
) :
    ViewModel() {
    val currentUser = accountsRepository.activeAccount.map { it.info!!.username }

    val userProfile: MutableState<UserDTO?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            val res = api.getUser(username)
            if (res.isSuccessful) {
                userProfile.value = res.body()?.data
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(username: String): ProfileViewModel
    }
}