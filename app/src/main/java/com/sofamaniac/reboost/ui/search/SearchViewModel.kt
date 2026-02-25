package com.sofamaniac.reboost.ui.search

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.domain.repository.PostSearchParams
import com.sofamaniac.reboost.domain.repository.PostSearchRepository
import com.sofamaniac.reboost.domain.repository.feed.FeedSource
import com.sofamaniac.reboost.ui.subreddit.FeedViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: PostSearchRepository,
    val visitedPostsDao: VisitedPostsDao,
) : ViewModel(),
    FeedViewModelInterface {
    private val queryState = TextFieldState()

    override var listState by mutableStateOf(LazyListState())


    val query: String get() = queryState.text as String

    private var searchJob: Job? = null

    private var _params = MutableStateFlow(
        PostSearchParams(
            query = ""
        )
    )
    val params: StateFlow<PostSearchParams> = _params.asStateFlow()

    private var feedSource: FeedSource<PostSearchParams>? = null
    override val data = Pager(
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

    fun onQueryUpdate(q: String) {
        queryState.edit { replace(0, length, q) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            search()
        }
    }

    fun setSubreddit(subreddit: String) {
        _params.update {
            it.copy(subreddit = subreddit)
        }
    }

    private fun search() {
        Log.d("SearchViewModel", "search: ${queryState.text}")
        _params.update {
            it.copy(query = queryState.text as String)
        }
        refresh()
    }

    override fun refresh() {
        feedSource?.invalidate()
        repository.refresh()
        listState = LazyListState()
    }

}