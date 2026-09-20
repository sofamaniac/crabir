package com.sofamaniac.crabir.ui.postFeed.subreddit

import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.SubredditCache
import com.sofamaniac.crabir.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.crabir.ui.postFeed.PostFeedViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SubredditViewModel(
    private val repository: SubredditPostsRepository,
    visitedPostsDao: VisitedPostsDao,
    communityDao: SubredditDao,
    private val subredditCache: SubredditCache,
    /** Subreddit's prefixed display name */
    @InjectedParam slug: String,
    @InjectedParam initialSort: Sort,
    @InjectedParam initialTimeframe: Timeframe?,
) : PostFeedViewModel<SubredditData>(
    repository,
    visitedPostsDao,
    communityDao,
    initialSort,
    initialTimeframe
) {

    private val _info = MutableStateFlow<SubredditData?>(null)
    val info = _info.asStateFlow()

    init {
        val slug = if (slug.contains("/")) slug else "r/$slug"
        repository.updateSubreddit(slug)
        viewModelScope.launch {
            _info.value = subredditCache.getBySlug(slug)
            if (_info.value == null) {
                _info.value = repository.getInfo()
                updateData(_info.value)
            }
        }
    }

    fun subscribe() {
        viewModelScope.launch {
            repository.subscribe()
            _info.value = repository.getInfo()
            updateData(_info.value)
        }
    }

    fun unsubscribe() {
        viewModelScope.launch {
            repository.unsubscribe()
            _info.value = repository.getInfo()
            updateData(_info.value)
        }
    }

    fun favorite(favorite: Boolean) {
        viewModelScope.launch {
            repository.favorite(favorite)
            _info.value = repository.getInfo()
            updateData(_info.value)
        }
    }

}
