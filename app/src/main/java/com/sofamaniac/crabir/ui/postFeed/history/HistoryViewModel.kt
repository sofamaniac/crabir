package com.sofamaniac.crabir.ui.postFeed.history

import android.util.Log
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.sofamaniac.crabir.data.local.dao.CommunityDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.model.votable.post.PostData
import com.sofamaniac.crabir.domain.repository.accounts.AccountsRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import com.sofamaniac.crabir.domain.repository.feed.HistoryParams
import com.sofamaniac.crabir.domain.repository.feed.HistoryRepository
import com.sofamaniac.crabir.ui.postFeed.FeedViewModelInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel


@KoinViewModel
class HistoryViewModel(
    private val repository: HistoryRepository,
    visitedPostsDao: VisitedPostsDao,
    private val communityRepository: CommunityDao<SubredditData>,
    accountsRepository: AccountsRepository,
) : ViewModel(), FeedViewModelInterface<PostData> {

    override val listState = LazyStaggeredGridState()
    override var needScrollToTop = false
    private var _filters = MutableStateFlow<(PostData) -> Boolean>({ true })
    override val filters: StateFlow<(PostData) -> Boolean> = _filters.asStateFlow()
    override fun updateFilters(filters: (PostData) -> Boolean) {
        _filters.value = filters
    }

    val accounts =
        accountsRepository.accounts.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val _ignoredAccounts: MutableStateFlow<Set<Fullname>> = MutableStateFlow(emptySet())
    val ignoredAccounts: StateFlow<Set<Fullname>> = _ignoredAccounts
    val params = ignoredAccounts.combine(accounts) { ignored, accounts ->
        HistoryParams(accounts.filter { !ignored.contains(it.name) }.map { it.name })
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HistoryParams(emptyList()))


    override fun refresh() {
        needScrollToTop = true
        feedSource?.invalidate()
        repository.refresh()
    }

    private var feedSource: FeedSource<HistoryParams, PostData>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
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
        ).flatMapLatest { pagingData ->
            filters.map { filters -> pagingData.filter(filters) }
        }

    suspend fun createViewEntity(name: String): CommunityViewEntity =
        withContext(Dispatchers.IO) {
            val info = communityRepository.getBySlug(name)
            if (info == null) {
                Log.e("PostFeedViewModel", "Failed to find $name in community repository")
                CommunityViewEntity(name, displayName = "")
            } else {
                CommunityViewEntity(name, displayName = info.displayNamePrefixed)
            }
        }

    fun updateData(data: SubredditData?) {
        Log.d("PostFeedViewModel", "updateData: $data")
        if (data == null) return
        viewModelScope.launch(Dispatchers.IO) {
            communityRepository.upsert(data)
        }
    }

    fun updateIgnored(accounts: Set<Fullname>) {
        _ignoredAccounts.value = accounts
    }


    override fun isPostRead(post: PostData): Boolean {
        return true
    }
}
