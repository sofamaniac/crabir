/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui.user

import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.AccountsRepository
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel(assistedFactory = ProfileViewModel.Factory::class)
class ProfileViewModel @AssistedInject constructor(
    accountsRepository: AccountsRepository,
    api: RedditAPIService,
    @Assisted username: String
) :
    ViewModel() {
    val currentUser = accountsRepository.activeAccount.map { it.info!!.username }

    val userProfile: MutableState<UserDTO?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            val res = api.getUser(username)
            if (res.isSuccessful) {
                userProfile.value = res.body()?.data
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(username: String): ProfileViewModel
    }
}
@HiltViewModel(assistedFactory = SavedViewModel.Factory::class)
class SavedViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: SavedRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<VotableData>(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): SavedViewModel
    }
}

@HiltViewModel(assistedFactory = OverviewViewModel.Factory::class)
class OverviewViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: OverviewRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<VotableData>(username, repository, visitedPostsDao) {
    @AssistedFactory
    interface Factory {
        fun create(username: String): OverviewViewModel
    }
}

@HiltViewModel(assistedFactory = CommentsViewModel.Factory::class)
class CommentsViewModel @AssistedInject constructor(
    @Assisted username: String,
    repository: CommentsRepository,
    visitedPostsDao: VisitedPostsDao,
) : ProfileFeedViewModel<CommentType>(username, repository, visitedPostsDao), SortProfileTab {
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
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao) {
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
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao) {
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
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao) {
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
) : ProfileFeedViewModel<PostData>(username, repository, visitedPostsDao), SortProfileTab {
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

abstract class ProfileFeedViewModel<T : VotableData>(
    val username: String,
    private val repository: FeedRepositoryCommon<ProfileFeedParams, T>,
    private val visitedPostsDao: VisitedPostsDao,
) : ViewModel(), FeedViewModelInterface<T> {

    override val entity: Flow<CommunityViewEntity?> = flowOf(null)

    override val listState = LazyStaggeredGridState()
    override var needScrollToTop = false
    protected val _params = MutableStateFlow(
        ProfileFeedParams(
            username = username,
            sort = ProfileSort.New,
            timeframe = null
        )
    )

    val params: StateFlow<ProfileFeedParams> = _params.asStateFlow()

    override fun refresh() {
        needScrollToTop = true
        feedSource?.invalidate()
        repository.refresh()
    }

    private var feedSource: FeedSource<ProfileFeedParams, T>? = null
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
        )

    override fun visitPost(post: PostData, visitedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = VisitedPostEntity(
                id = post.name,
                visitedAt = System.currentTimeMillis(),
                visitedBy = visitedBy
            )
            visitedPostsDao.insert(entity)
            Log.d("PostFeedViewModel", "visitPost: Post visited (${post.id})")
        }
    }

    override fun isPostRead(post: PostData): Boolean {
        return runBlocking(Dispatchers.IO) {
            visitedPostsDao.getPost(post.name) != null
        }
    }
}

interface SortProfileTab {
    fun updateSort(sort: ProfileSort, timeframe: Timeframe? = null)
}