/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.api.SubscribeAction
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetailsMapper
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.VotableRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class SubredditCache @Inject constructor() {
    private val cache = mutableMapOf<String, SubredditData>()
    fun save(subreddit: SubredditData) {
        cache[subreddit.displayName] = subreddit
    }

    fun get(displayName: String): SubredditData? = cache[displayName]
}

class SubredditPostsRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<FeedParams>(votableRepository, api) {
    private var currentSubreddit: String? = null

    suspend fun getInfo(): SubredditData? {
        if (currentSubreddit == null) {
            return null
        }
        val res = api.getSubInfo(currentSubreddit!!)
        if (!res.isSuccessful) {
            return null
        }
        return res.body()?.data?.let { SubredditDetailsMapper.map(it) }
    }

    fun updateSubreddit(subreddit: String) {
        currentSubreddit = subreddit
    }

    suspend fun subscribe(): Result<Unit> {
        val subreddit = currentSubreddit ?: return Result.failure(Exception("No subreddit set"))
        val res = api.subscribe(SubscribeAction.SUBSCRIBE, subreddit)
        return if (res.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to subscribe"))
        }
    }

    suspend fun unsubscribe(): Result<Unit> {
        val subreddit = currentSubreddit ?: return Result.failure(Exception("No subreddit set"))
        val res = api.subscribe(SubscribeAction.UNSUBSCRIBE, subreddit)
        return if (res.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to subscribe"))
        }
    }

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams
    ): PagedResponse<Fullname> {
        val subreddit = currentSubreddit ?: return PagedResponse()
        return makeRequest {
            api.getSubreddit(
                subreddit = subreddit,
                after = after,
                sort = params.sort,
                timeframe = params.timeframe
            )
        }
    }
}
