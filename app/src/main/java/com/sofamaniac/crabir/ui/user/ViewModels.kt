/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.user

import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.toEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.feed.FeedRepositoryCommon
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import com.sofamaniac.crabir.domain.repository.profile.CommentsRepository
import com.sofamaniac.crabir.domain.repository.profile.DownvotedRepository
import com.sofamaniac.crabir.domain.repository.profile.HiddenRepository
import com.sofamaniac.crabir.domain.repository.profile.ProfileFeedParams
import com.sofamaniac.crabir.domain.repository.profile.ProfileSort
import com.sofamaniac.crabir.domain.repository.profile.SavedRepository
import com.sofamaniac.crabir.domain.repository.profile.SubmittedRepository
import com.sofamaniac.crabir.domain.repository.profile.UpvotedRepository
import com.sofamaniac.crabir.ui.subreddit.FeedViewModelInterface
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
import kotlinx.coroutines.runBlocking

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
) : ProfileFeedViewModel(username, repository, visitedPostsDao), SortProfileTab {
    @AssistedFactory
    interface Factory {
        fun create(username: String): CommentsViewModel
    }

    override fun updateSort(sort: ProfileSort, timeframe: Timeframe?) {
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
) : ProfileFeedViewModel(username, repository, visitedPostsDao), SortProfileTab {
    @AssistedFactory
    interface Factory {
        fun create(username: String): SubmittedViewModel
    }

    override fun updateSort(sort: ProfileSort, timeframe: Timeframe?) {
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
}

abstract class ProfileFeedViewModel(
    val username: String,
    private val repository: FeedRepositoryCommon<ProfileFeedParams>,
    private val visitedPostsDao: VisitedPostsDao,
) : ViewModel(), FeedViewModelInterface {


    override val listState = LazyStaggeredGridState()
    protected val _params = MutableStateFlow(
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

    override fun visitPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.insert(post.toEntity())
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id})")
        }
    }

    override fun isPostRead(post: PostData): Boolean {
        return runBlocking(Dispatchers.IO) {
            visitedPostsDao.getPost(post.id) != null
        }
    }
}

interface SortProfileTab {
    fun updateSort(sort: ProfileSort, timeframe: Timeframe? = null)
}