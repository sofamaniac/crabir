/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:10 PM
 *
 */

package com.sofamaniac.reboost.ui.subreddit

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.entities.toEntity
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.repository.feed.FeedRepositoryCommon
import com.sofamaniac.reboost.domain.repository.feed.PostsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


abstract class PostFeedViewModel(
    private val repository: FeedRepositoryCommon,
    private val visitedPostsDao: VisitedPostsDao
) : ViewModel() {

    var listState by mutableStateOf(LazyListState())
    data class FeedParams(
        val sort: Sort,
        val timeframe: Timeframe?
    )

    private val _params = MutableStateFlow(
        FeedParams(
            sort = Sort.Best,
            timeframe = null,
        )
    )
    val params: StateFlow<FeedParams> = _params.asStateFlow()

    fun scrollToTop() {
        viewModelScope.launch {
            listState.scrollToItem(0)
        }
    }

    fun refresh() {
        postsSource?.invalidate()
        repository.refresh()
        viewModelScope.launch {
            listState.scrollToItem(0)
        }
    }

    private var postsSource: PostsSource? = null
    var data = Pager(
        config = PagingConfig(pageSize = 100),
        initialKey = "",
        pagingSourceFactory = {
            PostsSource(
                repository,
                params.value.sort,
                params.value.timeframe
            ).also { postsSource = it }
        }
    )
        .flow.cachedIn(
            viewModelScope
        )

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        val needRefresh = params.value.sort != sort || params.value.timeframe != timeframe
        _params.update {
            if (!needRefresh) it
            else {
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
        if (needRefresh) {
            refresh()
        }
    }

    fun visitPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.insert(post.toEntity())
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id.id})")
        }
    }
}