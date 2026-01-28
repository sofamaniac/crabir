package com.sofamaniac.reboost.data.repository

import android.util.Log
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.Thing.Listing
import com.sofamaniac.reboost.data.remote.dto.Thing.Subreddit
import com.sofamaniac.reboost.domain.model.PagedResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import retrofit2.Response

class SubscriptionsRepository(
    val api: RedditAPIService,
    val accountsRepository: AccountsRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    val subscriptions: StateFlow<List<Thing.Subreddit>> =
        accountsRepository.activeAccount
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
        accountsRepository.activeAccount
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
        request: suspend () -> Response<Listing<T>>
    ): PagedResponse<T> {
        val response = request()
        if (response.isSuccessful) {
            val listing = response.body()
            listing?.let {
                return PagedResponse(
                    data = it.data.children,
                    after = it.data.after,
                    total = it.size
                )
            }
            return PagedResponse()
        }
        Log.e("makeRequest", "Error making request : ${response.errorBody()}")
        return PagedResponse()
    }

    suspend fun getSubreddits(after: String): PagedResponse<Subreddit> {
        return makeRequest { api.getSubreddits(after = after) }
    }

    suspend fun loadSubscriptions(): List<Thing.Subreddit> {
        var after: String? = ""
        var subs: List<Thing.Subreddit> = emptyList()
        while (after != null) {
            val response = getSubreddits(after)
            after = response.data.lastOrNull()?.data?.name
            subs = subs.plus(response.data)
        }
        Log.d(
            "SubscriptionsRepository",
            "loadSubscriptions: ${subs.size} subreddits loaded"
        )
        return subs
    }

    suspend fun loadMultis(): List<Thing.Multi> {
        val response = api.getMultis()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            Log.e("SubscriptionsRepository", "Error loading multis: ${response.errorBody()}")
            return emptyList()
        }

    }
}