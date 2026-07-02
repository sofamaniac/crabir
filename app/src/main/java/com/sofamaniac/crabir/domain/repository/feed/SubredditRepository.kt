/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import android.util.Log
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.SubscribeAction
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.ViewModelScope


@Singleton
class SubredditCache(
    private val dao: SubredditRepository
) {
    suspend fun save(subreddit: SubredditData) {
        dao.upsert(subreddit)
    }

    suspend fun get(displayName: String): SubredditData? {
        return dao.getBySlug(displayName)
    }
}

@ViewModelScope
class SubredditPostsRepository(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService,
) : PostFeedRepository<FeedParams>() {
    private var currentSubreddit: String? = null
    private var info: SubredditData? = null

    suspend fun getInfo(): SubredditData? {
        if (currentSubreddit == null) {
            return null
        }
        try {
            val res = api.getSubInfo(currentSubreddit!!)
            if (!res.isSuccessful) {
                return null
            }
            info = SubredditDTOMapper.map(res.body()!!.data)
            return info
        } catch (e: Exception) {
            Log.e("SubredditPostsRepository", "Failed to get subreddit info", e)
            return null
        }
    }

    /**
     * @param subreddit the non prefixed display name of the subreddit
     * */
    fun updateSubreddit(subreddit: String) {
        currentSubreddit = subreddit
    }

    suspend fun subscribe(): Result<Unit> {
        val subreddit = info ?: return Result.failure(Exception("No info for subreddit"))
        val res = api.subscribe(SubscribeAction.SUBSCRIBE, subreddit.name)
        return if (res.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to subscribe"))
        }
    }

    suspend fun unsubscribe(): Result<Unit> {
        val subreddit = info ?: return Result.failure(Exception("No info for subreddit"))
        val res = api.subscribe(SubscribeAction.UNSUBSCRIBE, subreddit.name)
        return if (res.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to subscribe"))
        }
    }

    suspend fun favorite(favorite: Boolean): Result<Unit> {
        val subreddit = info ?: return Result.failure(Exception("No info for subreddit"))
        val res = api.favorite(subreddit.displayName, favorite)
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
