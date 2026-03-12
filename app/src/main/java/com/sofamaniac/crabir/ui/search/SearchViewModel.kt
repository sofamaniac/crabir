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
import androidx.paging.map
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.api.CommunitySearchSort
import com.sofamaniac.crabir.data.remote.api.PostSearchSort
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditData
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.domain.repository.DataInterface
import com.sofamaniac.crabir.domain.repository.ListingSource
import com.sofamaniac.crabir.domain.repository.search.CommentSearchRepository
import com.sofamaniac.crabir.domain.repository.search.CommunitySearchRepository
import com.sofamaniac.crabir.domain.repository.search.PostSearchRepository
import com.sofamaniac.crabir.domain.repository.search.SearchParams
import com.sofamaniac.crabir.domain.repository.search.SearchRepositoryGeneric
import com.sofamaniac.crabir.domain.repository.search.UserSearchRepository
import com.sofamaniac.crabir.ui.subreddit.FeedViewModelInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class SearchViewModel<Data : DataInterface>(
    val repository: SearchRepositoryGeneric<Data>,
    val initialParams: SearchParams,
) : ViewModel() {
    private val queryState = TextFieldState()

    val listState = LazyStaggeredGridState()


    val query: String get() = queryState.text as String

    private var searchJob: Job? = null


    internal var _params = MutableStateFlow(
        initialParams
    )
    val params: StateFlow<SearchParams> = _params.asStateFlow()

    private var feedSource: ListingSource<SearchParams, Data>? = null
    val items = Pager(
        config = PagingConfig(pageSize = 100, prefetchDistance = 10, initialLoadSize = 100),
        initialKey = "",
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
    @Assisted initialParams: SearchParams
) : SearchViewModel<PostData>(repository, initialParams), FeedViewModelInterface {

    override val data: StateFlow<PagingData<VotableData>> = items.map { pagingData ->
        pagingData.map { it as VotableData }
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.Lazily,
        initialValue = PagingData.empty()
    )

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

    @AssistedFactory
    interface Factory {
        fun create(
            params: SearchParams
        ): PostSearchViewModel
    }
}

@HiltViewModel
class CommunitySearchViewModel @Inject constructor(
    repository: CommunitySearchRepository,
) : SearchViewModel<SubredditData>(
    repository, initialParams =
        SearchParams(
            query = "",
            type = "sr",
            sort = CommunitySearchSort.Relevance
        )
) {
    fun setSort(sort: CommunitySearchSort) {
        _params.update {
            it.copy(sort = sort)
        }
        refresh()
    }
}

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    repository: UserSearchRepository,
) : SearchViewModel<UserDTO>(
    repository, initialParams =
        SearchParams(
            query = "",
            type = "user",
            sort = CommunitySearchSort.Relevance
        )
)

@HiltViewModel
class CommentSearchViewModel @Inject constructor(
    repository: CommentSearchRepository,
) : SearchViewModel<CommentData>(
    repository, initialParams =
        SearchParams(
            query = "",
            type = "comment",
            sort = CommunitySearchSort.Relevance
        )
)
