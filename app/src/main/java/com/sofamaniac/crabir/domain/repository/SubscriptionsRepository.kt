package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Thing.Subreddit
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.SubscribeAction
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.feed.MultiCache
import com.sofamaniac.crabir.domain.repository.feed.SubredditCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Singleton

@Singleton
class SubscriptionsRepository(
    val api: RedditAPIService,
    val accountsRepository: AccountsRepository,
    val subredditCache: SubredditCache,
    val multiCache: MultiCache,
) {
    val activeAccount = accountsRepository.activeAccount.distinctUntilChanged { old, new ->
        old.id == new.id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val subscriptions: StateFlow<List<Subreddit>> =
        activeAccount
            .flatMapLatest { account ->
                Log.d("SubscriptionsRepository", "activeAccount: $account")
                if (!account.isAnonymous()) {
                    flow { emit(loadSubscriptions()) }
                } else {
                    flowOf(emptyList())
                }
            }.stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val multis: StateFlow<List<Thing.Multi>> =
        activeAccount
            .flatMapLatest { account ->
                Log.d("SubscriptionsRepository", "activeAccount: $account")
                if (!account.isAnonymous()) {
                    flow { emit(loadMultis()) }
                } else {
                    flowOf(emptyList())
                }
            }.stateIn(
                scope = CoroutineScope(Dispatchers.IO),
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )


    private suspend fun <T : Thing> makeRequest(
        request: suspend () -> Result<Listing<T>>,
    ): PagedResponse<T> {
        val response = request()
        if (response.isSuccess) {
            val listing = response.getOrNull()
            return listing?.let {
                PagedResponse(
                    data = it.data.children,
                    after = it.data.after,
                    total = it.size
                )
            } ?: PagedResponse()
        }
        Log.e("makeRequest", "Error making request : ${response.exceptionOrNull()}")
        return PagedResponse()
    }

    suspend fun getSubreddits(after: Fullname): PagedResponse<Subreddit> {
        return makeRequest { api.getSubreddits(after = after) }
    }

    suspend fun loadSubscriptions(): List<Subreddit> {
        var after: Fullname? = Fullname("")
        var subs: List<Subreddit> = emptyList()
        while (after != null) {
            val response = runCatching { getSubreddits(after) }.getOrNull()
            after = response?.data?.lastOrNull()?.data?.name
            subs = subs.plus(response?.data ?: emptyList())
        }
        Log.d(
            "SubscriptionsRepository",
            "loadSubscriptions: ${subs.size} subreddits loaded"
        )
        for (sub in subs) {
            val mapped = SubredditDTOMapper.map(sub.data)
            subredditCache.insert(mapped)
        }
        return subs
    }

    suspend fun loadMultis(): List<Thing.Multi> {
        val result = runCatching { api.getMultireddits() }
        if (result.isFailure) {
            Log.e("SubscriptionsRepository", "Error loading multis: ${result.exceptionOrNull()}")
            return emptyList()
        }
        val response = result.getOrThrow()
        return if (response.isSuccess) {
            val multis = response.getOrNull() ?: emptyList()
            for (multi in multis) {
                multiCache.insert(multi.data)
            }
            multis
        } else {
            Log.e("SubscriptionsRepository", "Error loading multis: ${response.exceptionOrNull()}")
            emptyList()
        }
    }

    suspend fun subscribe(subreddit: SubredditData): Result<Unit> {
        return api.subscribe(SubscribeAction.SUBSCRIBE, subreddit.displayName).onSuccess {
            subredditCache.insert(subreddit.copy(userIsSubscriber = true))
        }
    }

    suspend fun unsubscribe(subreddit: SubredditData): Result<Unit> {
        return api.subscribe(SubscribeAction.UNSUBSCRIBE, subreddit.displayName).onSuccess {
            subredditCache.insert(subreddit.copy(userIsSubscriber = false))
        }
    }
}
