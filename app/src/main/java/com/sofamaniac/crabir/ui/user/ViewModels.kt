/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.user

import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.feed.FeedRepositoryCommon
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import com.sofamaniac.crabir.domain.repository.profile.CommentsRepository
import com.sofamaniac.crabir.domain.repository.profile.DownvotedRepository
import com.sofamaniac.crabir.domain.repository.profile.HiddenRepository
import com.sofamaniac.crabir.domain.repository.profile.OverviewRepository
import com.sofamaniac.crabir.domain.repository.profile.ProfileFeedParams
import com.sofamaniac.crabir.domain.repository.profile.ProfileSort
import com.sofamaniac.crabir.domain.repository.profile.SavedRepository
import com.sofamaniac.crabir.domain.repository.profile.SubmittedRepository
import com.sofamaniac.crabir.domain.repository.profile.UpvotedRepository
import com.sofamaniac.crabir.ui.postFeed.FeedViewModelInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    api: RedditAPIService,
    @InjectedParam username: String,
) : ViewModel() {
    val userProfile: MutableState<UserDTO?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            val res = api.getUser(username)
            if (res.isSuccess) {
                userProfile.value = res.getOrNull()?.data
            }
        }
    }
}


enum class SavedFilter {
    All,
    Posts,
    Comments;

    @Composable
    fun toStringResource(): String {
        return when (this) {
            All -> stringResource(R.string.saved_filter_all)
            Posts -> stringResource(R.string.saved_filter_posts)
            Comments -> stringResource(R.string.saved_filter_comments)
        }
    }
}

@KoinViewModel
class SavedViewModel(
    @InjectedParam username: String,
    repository: SavedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<VotableData>(username, repository, visitedPostsDao) {
    private val filterMut = MutableStateFlow(SavedFilter.All)
    val currentFilter: StateFlow<SavedFilter> = filterMut.asStateFlow()
    private val _filters: Flow<(VotableData) -> Boolean> = filterMut.map { filter ->
        Log.d("SavedViewModel", "Filter changed to $filter");
        { thing ->
            when (filter) {
                SavedFilter.All -> true
                SavedFilter.Posts -> thing is PostData
                SavedFilter.Comments -> thing is CommentType.Comment
            }
        }
    }
    override val filters = _filters.stateIn(viewModelScope, SharingStarted.Lazily, { true })

    fun updateFilter(filter: SavedFilter) {
        filterMut.value = filter
    }
}

@KoinViewModel
class OverviewViewModel(
    @InjectedParam username: String,
    repository: OverviewRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<VotableData>(username, repository, visitedPostsDao)

@KoinViewModel
class CommentsViewModel(
    @InjectedParam username: String,
    repository: CommentsRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<CommentType>(username, repository, visitedPostsDao), SortProfileTab {

    override fun updateSort(sort: ProfileSort, timeframe: Timeframe?) {
        val needRefresh = params.value.sort != sort || params.value.timeframe != timeframe
        paramsMut.update {
            if (!needRefresh) it
            else {
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
        if (needRefresh) {
            refresh()
        }
    }

}

@KoinViewModel
class UpvotedViewModel(
    @InjectedParam username: String,
    repository: UpvotedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao)

@KoinViewModel
class DownvotedViewModel(
    @InjectedParam username: String,
    repository: DownvotedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao)

@KoinViewModel
class HiddenViewModel(
    @InjectedParam username: String,
    repository: HiddenRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao)

@KoinViewModel
class SubmittedViewModel(
    @InjectedParam username: String,
    repository: SubmittedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao), SortProfileTab {

    override fun updateSort(sort: ProfileSort, timeframe: Timeframe?) {
        val needRefresh = params.value.sort != sort || params.value.timeframe != timeframe
        paramsMut.update {
            if (!needRefresh) it
            else {
                it.copy(sort = sort, timeframe = timeframe)
            }
        }
        if (needRefresh) {
            refresh()
        }
    }
}

abstract class ProfileFeedViewModel<T : VotableData>(
    val username: String,
    private val repository: FeedRepositoryCommon<ProfileFeedParams, T>,
    visitedPostsDao: VisitedPostsDao,
) : ViewModel(), FeedViewModelInterface<T> {

    private val _filters: MutableStateFlow<(T) -> Boolean> = MutableStateFlow { true }
    override val filters: StateFlow<(T) -> Boolean> = _filters.asStateFlow()
    override fun updateFilters(filters: (T) -> Boolean) {
        _filters.value = filters
    }

    override val listState = LazyStaggeredGridState()
    override var needScrollToTop = false
    protected val paramsMut = MutableStateFlow(
        ProfileFeedParams(
            username = username,
            sort = ProfileSort.New,
            timeframe = null
        )
    )

    private val history = visitedPostsDao.getHistoryFlow()
        .stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = emptyList())

    val params: StateFlow<ProfileFeedParams> = paramsMut.asStateFlow()

    override fun refresh() {
        needScrollToTop = true
        feedSource?.invalidate()
        repository.refresh()
    }

    private var feedSource: FeedSource<ProfileFeedParams, T>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override val data: Flow<PagingData<T>> = Pager(
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
        ).flatMapLatest { pagingData ->
            filters.map { filters -> pagingData.filter(filters) }
        }

    override fun isPostRead(post: PostData): Boolean {
        return history.value.contains(post.name)
    }

}

interface SortProfileTab {
    fun updateSort(sort: ProfileSort, timeframe: Timeframe? = null)
}
