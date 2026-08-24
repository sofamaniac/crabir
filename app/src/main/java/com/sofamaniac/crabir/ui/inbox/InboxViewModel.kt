package com.sofamaniac.crabir.ui.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Message
import com.sofamaniac.crabir.domain.repository.InboxFeed
import com.sofamaniac.crabir.domain.repository.InboxRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class InboxViewModel(
    private val repo: InboxRepository,
    private val api: RedditAPIService,
    @InjectedParam val feed: InboxFeed,
) :
    ViewModel(), MessageInteraction {
    private var feedSource =
        FeedSource(repo, feed)
    val data: Flow<PagingData<Message>> = Pager(
        config = PagingConfig(pageSize = 100, prefetchDistance = 10, initialLoadSize = 100),
        initialKey = Fullname(""),
        pagingSourceFactory = {
            FeedSource(repo, feed)
                .also { feedSource = it }
        }
    )
        .flow.cachedIn(
            viewModelScope
        )

    fun refresh() {
        feedSource.invalidate()
        repo.refresh()
    }

    override fun markRead(name: Fullname) {
        viewModelScope.launch {
            repo.read(name)
        }
    }

    fun unread(name: Fullname) {
        viewModelScope.launch {
            repo.unread(name)
        }
    }

    fun readAll() {
        viewModelScope.launch {
            repo.readAll()
        }
    }

    override fun blockAuthor(username: String) {
        viewModelScope.launch {
            api.block(username)
        }
    }
}

interface MessageInteraction {
    fun markRead(name: Fullname)
    fun blockAuthor(username: String)
}