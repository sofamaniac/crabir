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
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sofamaniac.reboost.data.local.dao.VisitedCommunityDao
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.entities.VisitedCommunityEntity
import com.sofamaniac.reboost.data.local.entities.toEntity
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.model.VotableData
import com.sofamaniac.reboost.domain.repository.feed.FeedParams
import com.sofamaniac.reboost.domain.repository.feed.FeedRepositoryCommon
import com.sofamaniac.reboost.domain.repository.feed.FeedSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


interface FeedViewModelInterface {
    var listState: LazyListState
    val data: Flow<PagingData<VotableData>>
    fun refresh()
}

abstract class PostFeedViewModel(
    private val id: String?,
    private val repository: FeedRepositoryCommon<FeedParams>,
    private val visitedPostsDao: VisitedPostsDao,
    private val visitedCommunityDao: VisitedCommunityDao,
) : ViewModel(), FeedViewModelInterface {

    init {
        if (id != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val entity = visitedCommunityDao.getCommunity(id!!)
                if (entity?.sort != null) {
                    _params.update {
                        it.copy(sort = entity.sort!!, timeframe = entity.timeframe)
                    }
                }
            }
        }
    }

    override var listState by mutableStateOf(LazyListState())
    private val _params = MutableStateFlow(
        FeedParams(
            sort = Sort.Best,
            timeframe = null,
        )
    )

    val params: StateFlow<FeedParams> = _params.asStateFlow()

    override fun refresh() {
        feedSource?.invalidate()
        repository.refresh()
        listState = LazyListState()
    }

    private var feedSource: FeedSource<FeedParams>? = null
    override val data: Flow<PagingData<VotableData>> = Pager(
        config = PagingConfig(pageSize = 100, prefetchDistance = 10, initialLoadSize = 100),
        initialKey = "",
        pagingSourceFactory = {
            FeedSource(
                repository,
                params.value,
            ).also { feedSource = it }
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
            if (id != null) {
                Log.d("PostFeedViewModel", "updateSort: Updating sort to $sort")
                viewModelScope.launch(Dispatchers.IO) {
                    visitedCommunityDao.insert(VisitedCommunityEntity(id!!, sort, timeframe, null))
                }
            }
            refresh()
        }
    }

    fun visitPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.insert(post.toEntity())
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id})")
        }
    }
}