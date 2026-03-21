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
import com.sofamaniac.crabir.data.local.dao.VisitedCommunityDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedCommunityEntity
import com.sofamaniac.crabir.data.local.entities.toEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.domain.repository.feed.FeedRepositoryCommon
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


interface FeedViewModelInterface {
    val listState: LazyStaggeredGridState
    val data: Flow<PagingData<VotableData>>
    val needScrollToTop: Boolean
    val entity: Flow<VisitedCommunityEntity?>

    fun refresh()
    fun visitPost(post: PostData)

    fun isPostRead(post: PostData): Boolean
}

abstract class PostFeedViewModel(
    private val id: String,
    private val repository: FeedRepositoryCommon<FeedParams>,
    private val visitedPostsDao: VisitedPostsDao,
    private val visitedCommunityDao: VisitedCommunityDao,
) : ViewModel(), FeedViewModelInterface {

    override val entity: Flow<VisitedCommunityEntity?> = flowOf(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val e = entity.firstOrNull()
            if (e?.sort != null) {
                _params.update {
                    it.copy(sort = e.sort, timeframe = e.timeframe)
                }
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

    private var feedSource: FeedSource<FeedParams>? = null
    override val data: Flow<PagingData<VotableData>> = Pager(
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

    fun updateData(data: SubredditData?) {
        Log.d("PostFeedViewModel", "updateData: $data")
        if (data == null) return
        viewModelScope.launch(Dispatchers.IO) {
            val e = entity.first()?.copy(data = data)
            if (e != null) {
                visitedCommunityDao.upsert(e)
            } else {
                visitedCommunityDao.insert(
                    VisitedCommunityEntity(
                        id = data.displayName,
                        data = null
                    ).copy(
                        data = data,
                    )
                )
            }
        }
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        val needRefresh = params.value.sort != sort || params.value.timeframe != timeframe
        _params.update {
            if (!needRefresh) it
            else {
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
        if (needRefresh) {
            Log.d("PostFeedViewModel", "updateSort: Updating sort to $sort")
            viewModelScope.launch(Dispatchers.IO) {
                val e = entity.first()
                    ?.let { it.copy(sort = sort, timeframe = timeframe, data = it.data) }
                if (e != null) {
                    visitedCommunityDao.upsert(e)
                } else {
                    visitedCommunityDao.insert(
                        VisitedCommunityEntity(
                            id,
                            sort,
                            timeframe,
                            null,
                            null,
                            null
                        )
                    )
                }

            }
            refresh()
        }
    }

    override fun visitPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.insert(post.toEntity())
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id})")
        }
    }

    override fun isPostRead(post: PostData): Boolean {
        return runBlocking(Dispatchers.IO) {
            visitedPostsDao.getPost(post.name) != null
        }
    }
}