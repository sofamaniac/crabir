package com.sofamaniac.crabir.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.FullscreenHandler
import com.sofamaniac.crabir.LocalNavController
import com.sofamaniac.crabir.SearchRoute
import com.sofamaniac.crabir.data.remote.api.CommunitySearchSort
import com.sofamaniac.crabir.data.remote.api.PostSearchSort
import com.sofamaniac.crabir.domain.repository.DataInterface
import com.sofamaniac.crabir.domain.repository.search.SearchParams
import com.sofamaniac.crabir.ui.SortMenu
import com.sofamaniac.crabir.ui.subreddit.PostFeedViewer
import com.sofamaniac.crabir.ui.subredditList.Tile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTab(
    searchQuery: SearchRoute,
    modifier: Modifier = Modifier,
) {
    val viewModels = listOf(
        hiltViewModel<PostSearchViewModel, PostSearchViewModel.Factory>(key = "PostSearch") { factory ->
            val query = if (searchQuery.flair.isNotBlank()) "flair:\"${searchQuery.flair}\"" else ""
            val subreddit = searchQuery.subreddit.ifBlank { null }
            val params = SearchParams(
                query = query,
                subreddit = subreddit,
                restrictSubreddit = subreddit != null,
                type = "link",
                sort = PostSearchSort.Relevance
            )
            factory.create(params)
        },
        hiltViewModel<CommunitySearchViewModel>(key = "CommunitySearch"),
        hiltViewModel<UserSearchViewModel>(key = "UserSearch"),
        hiltViewModel<CommentSearchViewModel>(key = "CommentSearch"),
    )
    val tabs = listOf("Posts", "Communities", "Users", "Comments")
    val scope = rememberCoroutineScope()
    val currentTab = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    FullscreenHandler {
        Box {
            Scaffold(
                topBar = { TopBar(viewModels[currentTab.currentPage], scrollBehavior) },
                bottomBar = {},
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                            Tab(
                                selected = index == currentTab.currentPage,
                                onClick = {
                                    val query = viewModels[currentTab.currentPage].query
                                    viewModels[index].onQueryUpdate(query)
                                    scope.launch { currentTab.animateScrollToPage(index) }
                                },
                                text = { Text(tab) })
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

                            is CommentSearchViewModel ->
                                InnerTab(viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InnerTab(viewModel: PostSearchViewModel) {
    PostFeedViewer(viewModel)
}

@Composable
private fun InnerTab(viewModel: CommunitySearchViewModel) {
    val things = viewModel.items.collectAsLazyPagingItems()
    val navController = LocalNavController.current!!
    val listState = viewModel.listState
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 8.dp,
        state = listState, modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = things.itemCount,
            key = things.itemKey { p -> p.id }) { index ->
            val subreddit = things[index]!!
            Tile(
                subreddit,
                modifier = Modifier.clickable {
                    navController.navigate(
                        com.sofamaniac.crabir.SubredditRoute(
                            subreddit.display_name
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun InnerTab(viewModel: UserSearchViewModel) {
    val things = viewModel.items.collectAsLazyPagingItems()
    val listState = viewModel.listState
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 8.dp,
        state = listState, modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = things.itemCount,
            key = things.itemKey { p -> p.id }) { index ->
            val user = things[index]!!
            Text(user.name)
        }
    }
}

@Composable
private fun InnerTab(viewModel: CommentSearchViewModel) {
    val things = viewModel.items.collectAsLazyPagingItems()
    val listState = viewModel.listState
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 8.dp,
        state = listState, modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = things.itemCount,
            key = things.itemKey { p -> p.id }) { index ->
            val comment = things[index]!!
            Text(comment.name)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    viewModel: SearchViewModel<out DataInterface>,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val navController = LocalNavController.current!!
    val state = rememberSearchBarState()
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            SearchBar(
                state,
                inputField = {
                    SearchBarDefaults.InputField(
                        query = viewModel.query,
                        onQueryChange = viewModel::onQueryUpdate,
                        onSearch = {},
                        expanded = false,
                        placeholder = { Text("Search") },
                        onExpandedChange = {}
                    )
                })
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            when (viewModel) {
                is PostSearchViewModel -> {
                    SortMenu<PostSearchSort> { sort, timeframe ->
                        viewModel.setSort(sort, timeframe)
                    }
                }

                is CommunitySearchViewModel -> {
                    SortMenu<CommunitySearchSort> { sort, _ ->
                        viewModel.setSort(sort)
                    }
                }

            }
        }
    )
}