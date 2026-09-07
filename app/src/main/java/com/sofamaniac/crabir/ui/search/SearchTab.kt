package com.sofamaniac.crabir.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.SortInterface
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.reddit.CommunitySearchSort
import com.sofamaniac.crabir.data.remote.reddit.PostSearchSort
import com.sofamaniac.crabir.domain.repository.search.PostSearchParams
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.SearchRoute
import com.sofamaniac.crabir.settings.helper.ListSelector
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.ListItem
import com.sofamaniac.crabir.ui.ThemedSwitch
import com.sofamaniac.crabir.ui.TimeframeMenu
import com.sofamaniac.crabir.ui.search.community.InnerTab
import com.sofamaniac.crabir.ui.subreddit.PostFeedViewer
import com.sofamaniac.crabir.ui.subreddit.PostView
import com.sofamaniac.crabir.ui.user.ProfileTabs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTab(
    searchQuery: SearchRoute,
    modifier: Modifier = Modifier,
    initialTab: Int = searchQuery.initialTab ?: if (searchQuery.flair.isNotBlank()) 0 else 1,
    commonViewModel: SearchCommonViewModel = koinViewModel {
        val initialQuery =
            if (searchQuery.flair.isNotBlank()) "flair:\"${searchQuery.flair}\"" else ""
        parametersOf(initialQuery)
    },
) {
    val viewModels = listOf(
        koinViewModel<PostSearchViewModel>(key = "PostSearch") {
            val subreddit = searchQuery.subreddit.ifBlank { null }
            val params = PostSearchParams(
                query = commonViewModel.query,
                subreddit = subreddit,
                restrictSubreddit = subreddit != null,
                type = "link",
                sort = PostSearchSort.Relevance
            )
            parametersOf(params)
        },
        koinViewModel<CommunitySearchViewModel>(key = "CommunitySearch"),
        koinViewModel<UserSearchViewModel>(key = "UserSearch"),
        //hiltViewModel<CommentSearchViewModel>(key = "CommentSearch"),
    )
    val tabs = listOf(
        stringResource(R.string.search_post_tab),
        stringResource(R.string.search_communities_tab),
        stringResource(R.string.search_users_tab)
    ) //, "Comments")
    val scope = rememberCoroutineScope()
    val currentTab = rememberPagerState(initialPage = initialTab, pageCount = { tabs.size })
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val showSettings by commonViewModel.showSettings.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            topBar = {
                TopBar(
                    commonViewModel,
                    scrollBehavior,
                    enableSettings = currentTab.currentPage != 2
                ) {
                    commonViewModel.onQueryUpdate(it)
                    viewModels[currentTab.currentPage].onQueryUpdate(it)
                }
            },
            bottomBar = {},
            modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .imePadding(),
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .fillMaxSize()
            ) {
                SecondaryTabRow(
                    selectedTabIndex = currentTab.currentPage,
                ) {
                    tabs.forEachIndexed { index, tab ->
                        val localViewModel = viewModels[index]
                        Tab(
                            selected = index == currentTab.currentPage,
                            onClick = {
                                val query = commonViewModel.query
                                localViewModel.onQueryUpdate(query)
                                scope.launch { currentTab.animateScrollToPage(index) }
                            },
                            text = { Text(tab) }
                        )

                    }
                }
                val localViewModel = viewModels[currentTab.currentPage]
                AnimatedVisibility(visible = showSettings) {
                    when (localViewModel) {
                        is PostSearchViewModel -> {
                            SearchSettings(localViewModel)
                        }

                        is CommunitySearchViewModel -> {
                            SearchSettings(localViewModel)
                        }
                    }
                }
                HorizontalPager(
                    state = currentTab,
                    modifier = Modifier
                        .fillMaxSize()
                ) { index ->
                    when (val viewModel = viewModels[index]) {
                        is PostSearchViewModel ->
                            InnerTab(viewModel)

                        is CommunitySearchViewModel ->
                            InnerTab(viewModel)

                        is UserSearchViewModel ->
                            InnerTab(viewModel)

                        //                        is CommentSearchViewModel ->
                        //                            InnerTab(viewModel)
                    }
                }
            }
        }
    }
}

@KoinViewModel
class SearchCommonViewModel(@InjectedParam query: String) : ViewModel() {
    val queryState = TextFieldState(initialText = query)
    val query: String get() = queryState.text as String
    var showSettings = MutableStateFlow(false)

    fun onQueryUpdate(q: String) {
        queryState.edit { replace(0, length, q) }
    }
}


@Composable
private fun InnerTab(
    viewModel: PostSearchViewModel,
) {
    PostFeedViewer(viewModel, viewEntity = null) { post, isMosVisible ->
        PostView(
            post,
            isMostVisible = isMosVisible,
            showHidden = false
        )
    }
}


@OptIn(ExperimentalUuidApi::class)
@Composable
private fun InnerTab(viewModel: UserSearchViewModel) {
    val things = viewModel.items.collectAsLazyPagingItems()
    val listState = viewModel.listState
    val navController = LocalNavController.current
    PullToRefreshBox(
        isRefreshing = things.loadState.refresh == LoadState.Loading,
        onRefresh = {
            viewModel.refresh()
        },
        modifier = Modifier
            .fillMaxSize(),
        indicator = {
            if (things.loadState.refresh == LoadState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            verticalItemSpacing = 2.dp,
            state = listState, modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = things.itemCount,
                key = things.itemKey { p ->
                    p.id
                }) { index ->
                val user = things[index]!!
                val iconUrl = if (user.prefShowSnoovatar) {
                    user.snoovatarImg?.ifBlank { user.iconImg }
                } else {
                    user.iconImg
                }
                ListItem(
                    modifier = Modifier.clickable {
                        navController?.navigate(
                            ProfileRoute(
                                user.username,
                                ProfileTabs.Overview,
                            )
                        )
                    },
                    leadingContent = {
                        AsyncImage(
                            model = iconUrl,
                            contentDescription = "${user.username} profile picture",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }, content = {
                        Text(user.username)
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    commonViewModel: SearchCommonViewModel,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    enableSettings: Boolean = true,
    onQueryUpdate: (String) -> Unit = {},
) {
    val navController = LocalNavController.current
    val showSettings by commonViewModel.showSettings.collectAsState()
    val focusRequest = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        if (commonViewModel.query.isEmpty()) {
            focusRequest.requestFocus()
        }
    }
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            TextField(
                singleLine = true,
                value = commonViewModel.query,
                onValueChange = onQueryUpdate,
                modifier = Modifier.focusRequester(focusRequest),
            )
        },
        navigationIcon = {
            BackButton {
                navController?.popBackStack()
            }
        },
        actions = {
            IconButton(
                enabled = enableSettings,
                onClick = { commonViewModel.showSettings.value = !showSettings }
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Search Settings")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified Sort> SortMenu(
    currentValue: Sort,
    currentTimeframe: Timeframe? = null,
    crossinline onSelect: (Sort, Timeframe?) -> Unit,
) where Sort : Enum<Sort>, Sort : SortInterface {
    val sortString = stringResource(currentValue.representation)
    val timeframeString = currentTimeframe?.let { stringResource(it.representation) }
    val currentValueString =
        if (timeframeString == null) sortString else "$sortString ($timeframeString)"
    var showMenu by remember { mutableStateOf(false) }
    val entries = enumValues<Sort>()
    var timeframeExpanded by remember { mutableStateOf(false) }
    var chosenSort by remember { mutableStateOf<Sort?>(null) }
    ListItem(
        leadingContent = {
            Icon(Icons.AutoMirrored.Default.Sort, contentDescription = null)
        },
        content = { Text(stringResource(R.string.sort)) },
        trailingContent = {
            ExposedDropdownMenuBox(
                expanded = showMenu,
                onExpandedChange = { showMenu = it },
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                OutlinedTextField(
                    value = currentValueString,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = showMenu
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        )
                )
                ExposedDropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }) {
                    entries.forEach { sort ->
                        if (sort.isTimeframe) {
                            DropdownMenuItem(
                                text = { Text(stringResource(sort.representation)) },
                                onClick = {
                                    timeframeExpanded = true
                                    chosenSort = sort
                                },
                                trailingIcon = {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowRight,
                                        contentDescription = "Select"
                                    )
                                })
                        } else {
                            DropdownMenuItem(
                                text = { Text(stringResource(sort.representation)) },
                                onClick = {
                                    onSelect(sort, null)
                                    showMenu = false
                                })
                        }
                    }

                }
            }
            TimeframeMenu(
                expanded = timeframeExpanded,
                onDismiss = { timeframeExpanded = false }
            ) {
                onSelect(chosenSort!!, it)
                showMenu = false
                timeframeExpanded = false
            }
        }
    )
}

@Composable
fun SearchSettings(viewModel: PostSearchViewModel) {
    val params by viewModel.params.collectAsState()
    Column {
        ListSelector(
            PostSearchSort.entries,
            params.sort,
            onOptionSelected = viewModel::setSort,
            headlineContent = { Text(stringResource(R.string.sort)) }
        )
        if (params.subreddit != null) {
            ListItem(
                modifier = Modifier.clickable {
                    viewModel.setRestrictSubreddit(!params.restrictSubreddit)
                },
                content = { Text(stringResource(R.string.restrict_subreddit)) },
                trailingContent = {
                    ThemedSwitch(
                        checked = params.restrictSubreddit,
                        onCheckedChange = {
                            viewModel.setRestrictSubreddit(it)
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun SearchSettings(viewModel: CommunitySearchViewModel) {
    val params by viewModel.params.collectAsState()
    ListSelector(
        CommunitySearchSort.entries,
        params.sort as CommunitySearchSort,
        onOptionSelected = viewModel::setSort,
        headlineContent = { Text(stringResource(R.string.sort)) }
    )
}
