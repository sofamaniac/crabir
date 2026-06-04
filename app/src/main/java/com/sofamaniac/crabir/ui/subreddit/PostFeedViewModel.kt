/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 10:10 PM
 *
 */

package com.sofamaniac.crabir.ui.subreddit

import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sofamaniac.crabir.data.local.dao.CommunityDao
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.local.entities.toEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import com.sofamaniac.crabir.domain.repository.feed.PostFeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


interface FeedViewModelInterface<T : VotableData> {
    val listState: LazyStaggeredGridState
    val data: Flow<PagingData<T>>
    var needScrollToTop: Boolean
    val entity: Flow<CommunityViewEntity?>

    fun refresh()
    fun visitPost(post: PostData)

    fun isPostRead(post: PostData): Boolean
}

abstract class PostFeedViewModel<T>(
    private val id: Fullname,
    private val repository: PostFeedRepository<FeedParams>,
    private val visitedPostsDao: VisitedPostsDao,
    private val communityDao: CommunityDao<T>,
    private val viewDao: CommunityViewDao,
) : ViewModel(), FeedViewModelInterface<PostData> {

    override val entity: Flow<CommunityViewEntity?> = viewDao.getCommunityFlow(id)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val e = entity.firstOrNull()
            Log.d("PostFeedViewModel", "init: $e")
            if (e?.sort != null) {
                updateSort(e.sort, e.timeframe)
            }
        }
    }

    override val listState = LazyStaggeredGridState()
    override var needScrollToTop = false
    private val _params = MutableStateFlow(
        FeedParams(
            sort = Sort.Best,
            timeframe = null,
        )
    )

    val params: StateFlow<FeedParams> = _params.asStateFlow()

    override fun refresh() {
        needScrollToTop = true
        feedSource?.invalidate()
        repository.refresh()
    }

    private var feedSource: FeedSource<FeedParams, PostData>? = null
    override val data: Flow<PagingData<PostData>> = Pager(
        config = PagingConfig(pageSize = 100, prefetchDistance = 10, initialLoadSize = 100),
        initialKey = Fullname(""),
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

    fun updateData(data: T?) {
        Log.d("PostFeedViewModel", "updateData: $data")
        if (data == null) return
        viewModelScope.launch(Dispatchers.IO) {
            communityDao.upsert(data)
        }
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        val needRefresh = params.value.sort != sort || params.value.timeframe != timeframe
        Log.d("PostFeedViewModel", "_params: ${params.value}, sort: $sort, timeframe: $timeframe")
        _params.update {
            if (!needRefresh) it
            else {
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
        if (needRefresh) {
            Log.d("PostFeedViewModel", "updateSort: Updating sort to $sort")
            viewModelScope.launch(Dispatchers.IO) {
                val view = entity.first()?.copy(sort = sort, timeframe = timeframe)
                if (view != null) {
                    viewDao.update(view)
                }

            }
            refresh()
        }
    }

    override fun visitPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.insert(post.toEntity(System.currentTimeMillis()))
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id})")
        }
    }

    override fun isPostRead(post: PostData): Boolean {
        return runBlocking(Dispatchers.IO) {
            visitedPostsDao.getPost(post.name) != null
        }
    }
}