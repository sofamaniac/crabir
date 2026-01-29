/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.thread

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.toRoute
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.PostRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadView(
    modifier: Modifier = Modifier,
    permalink: String? = null,
    dismiss: () -> Unit = {},
) {
    val link = permalink
        ?: LocalNavController.current?.currentBackStackEntry?.toRoute<PostRoute>()?.postPermalink

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val viewModel: ThreadViewModel =
        hiltViewModel<ThreadViewModel, ThreadViewModel.Factory> { factory ->
            factory.create(link!!)
        }

    Scaffold(
        topBar = { TopBar(viewModel, scrollBehavior, dismiss) },
        //bottomBar = { TabBar(selected) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        CommentListRoot(
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

//data class CommentsViewModel(val post: PostData, val initialSort: Sort = Sort.Best) : ViewModel() {
//    var comments by mutableStateOf<Thing.Listing<Thing>>(emptyListing())
//    var isRefreshing by mutableStateOf(false)
//    var listState by mutableStateOf(LazyListState())
//    var sort: Sort by mutableStateOf(initialSort)
//
//    suspend fun refresh() {
//        isRefreshing = true
//        comments =
//            RedditAPI.service.getComments(post.subreddit.name, post.id, sort = sort)
//                .body()?.comments ?: emptyListing()
//        isRefreshing = false
//    }
//}
//
//class CommentsViewModelFactory(private val post: PostData) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CommentsViewModel(post) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}