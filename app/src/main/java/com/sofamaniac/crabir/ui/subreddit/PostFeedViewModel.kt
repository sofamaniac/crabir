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
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.CommunityData
import com.sofamaniac.crabir.domain.model.DUMMY_POST
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.feed.FeedParams
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import com.sofamaniac.crabir.domain.repository.feed.PostFeedRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface FeedViewModelInterface<T : VotableData> {
    val listState: LazyStaggeredGridState
    val data: Flow<PagingData<T>>
    var needScrollToTop: Boolean

    fun refresh()

    fun isPostRead(post: PostData): Boolean
}

object FeedViewModelInterfacePreview : FeedViewModelInterface<PostData> {
    override val listState: LazyStaggeredGridState = LazyStaggeredGridState()
    override val data: Flow<PagingData<PostData>> =
        flowOf(
            PagingData.from(
                List(100) {
                    DUMMY_POST.copy(
                        id = it.toString(),
                        name = Fullname(it.toString())
                    )
                }
            )
        )
    override var needScrollToTop: Boolean = false

    override fun refresh() = Unit

    override fun isPostRead(post: PostData): Boolean {
        return false
    }
}

abstract class PostFeedViewModel<T : CommunityData>(
    private val repository: PostFeedRepository<FeedParams>,
    visitedPostsDao: VisitedPostsDao,
    private val communityRepository: CommunityDao<T>,
    viewEntity: CommunityViewEntity,
) : ViewModel(), FeedViewModelInterface<PostData> {

    override val listState = LazyStaggeredGridState()
    override var needScrollToTop = false
    private val _params = MutableStateFlow(
        FeedParams(
            sort = viewEntity.sort!!,
            timeframe = viewEntity.timeframe,
        )
    )

    val params: StateFlow<FeedParams> = _params.asStateFlow()

    val history = visitedPostsDao.getHistoryFlow()
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())

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

    open suspend fun createViewEntity(name: String): CommunityViewEntity {
        val info = communityRepository.getBySlug(name)
        if (info == null) {
            Log.e("PostFeedViewModel", "Failed to find $name in community repository")
            return CommunityViewEntity(name, displayName = "")
        } else {
            return CommunityViewEntity(name, displayName = info.displayNamePrefixed)
        }
    }

    fun updateData(data: T?) {
        Log.d("PostFeedViewModel", "updateData: $data")
        if (data == null) return
        viewModelScope.launch(Dispatchers.IO) {
            communityRepository.upsert(data)
        }
    }

    fun updateSort(sort: Sort, timeframe: Timeframe? = null) {
        val needRefresh = params.value.sort != sort || params.value.timeframe != timeframe
        Log.d("PostFeedViewModel", "_params: ${params.value}, sort: $sort, timeframe: $timeframe")
        _params.update {
            if (!needRefresh) {
                it
            } else {
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
        if (needRefresh) {
            Log.d("PostFeedViewModel", "updateSort: Updating sort to $sort")
            refresh()
        }
    }

    override fun isPostRead(post: PostData): Boolean {
        return history.value.contains(post.name)
    }
}
