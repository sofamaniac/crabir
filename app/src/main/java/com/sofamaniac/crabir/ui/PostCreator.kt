package com.sofamaniac.crabir.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sofamaniac.crabir.LocalFullscreenHandler
import com.sofamaniac.crabir.data.remote.api.PostSubmissionBuilder
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditData
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.ui.markdown.Editor
import com.sofamaniac.crabir.ui.search.CommunitySearchViewModel
import com.sofamaniac.crabir.ui.subredditList.Tile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreator(
    community: String? = null,
    kind: Kind = Kind.Self,
    viewModel: PostCreatorViewModel = hiltViewModel()
) {
    LaunchedEffect(community, kind) {
        if (community != null) {
            viewModel.state.subreddit = community
        }
        viewModel.state.kind = kind
    }
    val fullscreenManager = LocalFullscreenHandler.current!!
    val submission = viewModel.state
    val textState = rememberTextFieldState()
    val titleState = rememberTextFieldState()
    Editor(state = textState, topBar = {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { fullscreenManager.pop() }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            },
            title = { Text("Create post") },
            actions = {
                IconButton(onClick = {
                    if (viewModel.community == null) return@IconButton
                    submission.text = textState.text as String
                    submission.title = titleState.text as String
                    submission.subreddit = viewModel.community!!.display_name
                    viewModel.submit()
                    fullscreenManager.pop()
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            }
        )
    }) {
        CommunitySelector(viewModel)
        TextField(
            state = titleState,
            placeholder = { Text("Title") },
            inputTransformation = InputTransformation.maxLength(300)
        )
        if (submission.subreddit.isNotBlank()) {
            TextButton(onClick = {}) {
                Text("Flair")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                onClick = {
                    submission.nsfw = !submission.nsfw
                },
                selected = submission.nsfw,
                label = {
                    Text("NSFW")
                }
            )
            FilterChip(
                onClick = {
                    submission.spoiler = !submission.spoiler
                },
                selected = submission.spoiler,
                label = {
                    Text("SPOILER")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunitySelector(
    viewModel: PostCreatorViewModel,
    modifier: Modifier = Modifier,
) {
    val fullscreenManager = LocalFullscreenHandler.current!!
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                fullscreenManager.push(
                    { CommunitySearch(viewModel) },
                )
            }
            .padding(16.dp)
    ) {
        if (viewModel.community == null) {
            Text("Community")
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Search"
                )
            }
        } else {
            Tile(viewModel.community!!)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {}) { Text("RULES") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CommunitySearch(
    viewModel: PostCreatorViewModel,
    searchViewModel: CommunitySearchViewModel = hiltViewModel()
) {
    val searchState = rememberSearchBarState()
    val searchedCommunities = searchViewModel.items.collectAsLazyPagingItems()
    val subscriptions by searchViewModel.subscriptions.collectAsState()
    val listState = searchViewModel.listState
    val fullscreenManager = LocalFullscreenHandler.current!!
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { fullscreenManager.pop() }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Close search"
                        )
                    }
                },
                title = {
                    SearchBar(
                        searchState,
                        inputField = {
                            SearchBarDefaults.InputField(
                                query = searchViewModel.query,
                                onQueryChange = searchViewModel::onQueryUpdate,
                                onSearch = {},
                                expanded = false,
                                placeholder = { Text("Search") },
                                onExpandedChange = {}
                            )
                        }
                    )
                })
        }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            verticalItemSpacing = 8.dp,
            state = listState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (searchedCommunities.itemCount > 0) {
                items(
                    count = searchedCommunities.itemCount,
                    key = searchedCommunities.itemKey { p -> p.id }) { index ->
                    val subreddit = searchedCommunities[index]!!
                    Tile(subreddit, modifier = Modifier.clickable {
                        viewModel.community = subreddit
                        fullscreenManager.pop()
                    })
                }
            } else {
                val subs = subscriptions.sortedBy { it.data.display_name.lowercase() }
                items(
                    count = subs.size,
                    key = { subs[it].id }) { index ->
                    val subreddit = subs[index].data
                    Tile(subreddit, modifier = Modifier.clickable {
                        viewModel.community = subreddit
                        fullscreenManager.pop()
                    })
                }
            }
        }

    }
}

@HiltViewModel
class PostCreatorViewModel @Inject constructor(private val api: RedditAPIService) : ViewModel() {
    val state by mutableStateOf(PostSubmissionBuilder())
    var community: SubredditData? by mutableStateOf(null)

    var rules: List<String> by mutableStateOf(emptyList())
    var flairs: List<String> by mutableStateOf(emptyList())

    fun submit(): Result<Unit> {
        val submission = state.build()
        if (submission.isFailure) {
            Log.e("PostCreatorViewModel", "submit: ${submission.exceptionOrNull()}")
            return Result.failure(submission.exceptionOrNull()!!)
        }
        Log.d("PostCreatorViewModel", "submit: $submission")
        viewModelScope.launch {
            api.submitPost(submission.getOrThrow())
        }
        return Result.success(Unit)
    }
}