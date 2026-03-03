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
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.repository.ListingSource
import com.sofamaniac.reboost.domain.repository.search.SearchParams
import com.sofamaniac.reboost.domain.repository.search.SearchRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SearchViewModel.Factory::class)
class SearchViewModel @AssistedInject constructor(
    val repository: SearchRepository,
    val visitedPostsDao: VisitedPostsDao,
    @Assisted val initialParams: SearchParams,
) : ViewModel() {
    private val queryState = TextFieldState()

    var listState by mutableStateOf(LazyListState())


    val query: String get() = queryState.text as String

    private var searchJob: Job? = null


    private var _params = MutableStateFlow(
        initialParams
    )
    val params: StateFlow<SearchParams> = _params.asStateFlow()

    private var feedSource: ListingSource<SearchParams, Thing>? = null
    val data = Pager(
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
        listState = LazyListState()
    }

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

    @AssistedFactory
    interface Factory {
        fun create(
            params: SearchParams
        ): SearchViewModel
    }
}