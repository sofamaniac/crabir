/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import android.util.Log
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetailsMapper
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.SubscribeAction
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton


@Singleton
class SubredditCache @Inject constructor(
    private val dao: SubredditDao
) {
    suspend fun save(subreddit: SubredditData) {
        dao.upsert(subreddit)
    }

    suspend fun get(name: Fullname): SubredditData? {
        return dao.getByName(name)
    }
}

class SubredditPostsRepository @Inject constructor(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService,
) : PostFeedRepository<FeedParams>() {
    private var currentSubreddit: String? = null

    suspend fun getInfo(): SubredditData? {
        if (currentSubreddit == null) {
            return null
        }
        try {
            val res = api.getSubInfo(currentSubreddit!!)
            if (!res.isSuccessful) {
                return null
            }
            return res.body()?.data?.let { SubredditDetailsMapper.map(it) }
        } catch (e: Exception) {
            Log.e("SubredditPostsRepository", "Failed to get subreddit info", e)
            return null
        }
    }

    fun updateSubreddit(subreddit: Fullname) {
        currentSubreddit = subreddit.name
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

    suspend fun favorite(favorite: Boolean): Result<Unit> {
        val subreddit = currentSubreddit ?: return Result.failure(Exception("No subreddit set"))
        val res = api.favorite(subreddit, favorite)
        return if (res.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to favorite"))
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
