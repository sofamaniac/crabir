package com.sofamaniac.crabir.ui.search

import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.text.input.TextFieldState
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
import com.sofamaniac.crabir.data.remote.reddit.CommunitySearchSort
import com.sofamaniac.crabir.data.remote.reddit.PostSearchSort
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.DataInterface
import com.sofamaniac.crabir.domain.repository.ListingRepository
import com.sofamaniac.crabir.domain.repository.ListingSource
import com.sofamaniac.crabir.domain.repository.SubscriptionsRepository
import com.sofamaniac.crabir.domain.repository.search.CommunitySearchParams
import com.sofamaniac.crabir.domain.repository.search.CommunitySearchRepository
import com.sofamaniac.crabir.domain.repository.search.PostSearchParams
import com.sofamaniac.crabir.domain.repository.search.PostSearchRepository
import com.sofamaniac.crabir.domain.repository.search.UserSearchRepository
import com.sofamaniac.crabir.ui.subreddit.FeedViewModelInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface SearchParams<This> {
    val query: String
    fun copy(query: String = this.query): This
}

abstract class SearchViewModel<Params : SearchParams<Params>, Data : DataInterface>(
    val repository: ListingRepository<Params, Data>,
    val initialParams: Params,
) : ViewModel() {
    private val queryState = TextFieldState(initialText = initialParams.query)
    var showSettings = MutableStateFlow(false)

    val listState = LazyStaggeredGridState()



    val query: String get() = queryState.text as String

    private var searchJob: Job? = null


    internal var _params = MutableStateFlow(
        initialParams
    )
    val params: StateFlow<Params> = _params.asStateFlow()

    private var feedSource: ListingSource<Params, Data>? = null
    val items = Pager(
        config = PagingConfig(pageSize = 100, prefetchDistance = 10, initialLoadSize = 100),
        initialKey = Fullname(""),
        pagingSourceFactory = {
            ListingSource(
                repository,
                params.value,
            ).also { feedSource = it }
        }
    )
        .flow.cachedIn(
            viewModelScope
        )

    fun onQueryUpdate(q: String) {
        if (queryState.text as String == q) return
        queryState.edit { replace(0, length, q) }
        if (q.length < 3) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            search()
        }
    }


    private fun search() {
        Log.d("SearchViewModel", "search: ${queryState.text}")
        _params.update {
            it.copy(query = queryState.text as String)
        }
        refresh()
    }

    fun refresh() {
        feedSource?.invalidate()
        repository.refresh()
    }

}

@HiltViewModel(assistedFactory = PostSearchViewModel.Factory::class)
class PostSearchViewModel @AssistedInject constructor(
    repository: PostSearchRepository,
    val visitedPostsDao: VisitedPostsDao,
    @Assisted initialParams: PostSearchParams,
) : SearchViewModel<PostSearchParams, PostData>(repository, initialParams),
    FeedViewModelInterface<PostData> {
    override val entity: Flow<CommunityViewEntity?> = flowOf(null)
    override val data: StateFlow<PagingData<PostData>> = items.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.Lazily,
        initialValue = PagingData.empty()
    )

    override var needScrollToTop = false

    fun setSubreddit(subreddit: String) {
        _params.update {
            it.copy(subreddit = subreddit, restrictSubreddit = true)
        }
        refresh()
    }

    fun setRestrictSubreddit(restrict: Boolean) {
        _params.update {
            it.copy(restrictSubreddit = restrict)
        }
        refresh()
    }

    fun setSort(sort: PostSearchSort, timeframe: Timeframe? = null) {
        _params.update {
            it.copy(sort = sort, timeframe = timeframe)
        }
        refresh()
    }

    override fun visitPost(post: PostData, visitedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = VisitedPostEntity(post.name, System.currentTimeMillis(), visitedBy)
            visitedPostsDao.insert(entity)
        }
    }

    override fun isPostRead(post: PostData): Boolean {
        return runBlocking(Dispatchers.IO) {
            visitedPostsDao.getPost(post.name) != null
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            params: PostSearchParams
        ): PostSearchViewModel
    }
}

@HiltViewModel
class CommunitySearchViewModel @Inject constructor(
    repository: CommunitySearchRepository,
    subscriptionsRepository: SubscriptionsRepository,
) : SearchViewModel<CommunitySearchParams, SubredditData>(
    repository, initialParams =
        CommunitySearchParams(
            query = "",
            sort = CommunitySearchSort.Relevance,
            includeOver18 = true,
        )
) {


    val subscriptions = subscriptionsRepository.subscriptions
    fun setSort(sort: CommunitySearchSort) {
        _params.update {
            it.copy(sort = sort)
        }
        refresh()
    }

    fun setIncludeOver18(include: Boolean) {
        _params.update {
            it.copy(includeOver18 = include)
        }
    }
}

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    repository: UserSearchRepository,
) : SearchViewModel<PostSearchParams, UserDTO>(
    repository, initialParams =
        PostSearchParams(
            query = "",
            type = "user",
            sort = PostSearchSort.Relevance
        )
)

//@HiltViewModel
//class CommentSearchViewModel @Inject constructor(
//    repository: CommentSearchRepository,
//) : SearchViewModel<CommentData>(
//    repository, initialParams =
//        SearchParams(
//            query = "",
//            type = "comment",
//            sort = CommunitySearchSort.Relevance
//        )
//)
