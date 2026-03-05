/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui.user

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
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.entities.toEntity
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.model.VotableData
import com.sofamaniac.reboost.domain.repository.feed.FeedRepositoryCommon
import com.sofamaniac.reboost.domain.repository.feed.FeedSource
import com.sofamaniac.reboost.domain.repository.profile.CommentsRepository
import com.sofamaniac.reboost.domain.repository.profile.DownvotedRepository
import com.sofamaniac.reboost.domain.repository.profile.HiddenRepository
import com.sofamaniac.reboost.domain.repository.profile.ProfileFeedParams
import com.sofamaniac.reboost.domain.repository.profile.ProfileSort
import com.sofamaniac.reboost.domain.repository.profile.SavedRepository
import com.sofamaniac.reboost.domain.repository.profile.SubmittedRepository
import com.sofamaniac.reboost.domain.repository.profile.UpvotedRepository
import com.sofamaniac.reboost.ui.subreddit.FeedViewModelInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SavedViewModel.Factory::class)
class SavedViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: SavedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): SavedViewModel
    }
}

@HiltViewModel(assistedFactory = CommentsViewModel.Factory::class)
class CommentsViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: CommentsRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): CommentsViewModel
    }
}

@HiltViewModel(assistedFactory = UpvotedViewModel.Factory::class)
class UpvotedViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: UpvotedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): UpvotedViewModel
    }
}

@HiltViewModel(assistedFactory = DownvotedViewModel.Factory::class)
class DownvotedViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: DownvotedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): DownvotedViewModel
    }
}

@HiltViewModel(assistedFactory = HiddenViewModel.Factory::class)
class HiddenViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: HiddenRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): HiddenViewModel
    }
}

@HiltViewModel(assistedFactory = SubmittedViewModel.Factory::class)
class SubmittedViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: SubmittedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): SubmittedViewModel
    }
}

abstract class ProfileFeedViewModel(
    val username: String,
    private val repository: FeedRepositoryCommon<ProfileFeedParams>,
    private val visitedPostsDao: VisitedPostsDao,
) : ViewModel(), FeedViewModelInterface {


    override var listState by mutableStateOf(LazyListState())
    private val _params = MutableStateFlow(
        ProfileFeedParams(
            username = username,
            sort = ProfileSort.New,
            timeframe = null
        )
    )

    val params: StateFlow<ProfileFeedParams> = _params.asStateFlow()

    override fun refresh() {
        feedSource?.invalidate()
        repository.refresh()
        listState = LazyListState()
    }

    private var feedSource: FeedSource<ProfileFeedParams>? = null
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

    fun updateSort(sort: ProfileSort, timeframe: Timeframe? = null) {
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
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id})")
        }
    }
}
