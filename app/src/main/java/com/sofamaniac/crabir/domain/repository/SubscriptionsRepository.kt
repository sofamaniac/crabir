package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.Thing.Listing
import com.sofamaniac.crabir.data.remote.dto.Thing.Subreddit
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
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
            listing?.let {
                return PagedResponse(
                    data = it.data.children,
                    after = it.data.after,
                    total = it.size
                )
            }
            return PagedResponse()
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
            subredditCache.save(mapped)
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
        if (response.isSuccess) {
            return response.getOrNull() ?: emptyList()
        } else {
            Log.e("SubscriptionsRepository", "Error loading multis: ${response.exceptionOrNull()}")
            return emptyList()
        }

    }
}