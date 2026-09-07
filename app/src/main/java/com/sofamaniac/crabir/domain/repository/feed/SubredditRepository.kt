/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.local.dao.MultiDao
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.SubscribeAction
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.CommunityCache
import com.sofamaniac.crabir.domain.repository.LinksRepository
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.ViewModelScope


@Singleton
class SubredditCache(dao: SubredditDao) : CommunityCache<SubredditData>(dao)

@Singleton
class MultiCache(dao: MultiDao) : CommunityCache<MultiData>(dao)

@ViewModelScope
class SubredditPostsRepository(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService,
) : PostFeedRepository<FeedParams>() {
    private var currentSubreddit: String? = null
    private var info: SubredditData? = null

    suspend fun getInfo(): SubredditData? {
        return currentSubreddit?.let { sub ->
            api.getSubInfo(sub).map { res ->
                SubredditDTOMapper.map(res.data)
            }.getOrNull()
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
        return res
    }

    suspend fun unsubscribe(): Result<Unit> {
        val subreddit = info ?: return Result.failure(Exception("No info for subreddit"))
        val res = api.subscribe(SubscribeAction.UNSUBSCRIBE, subreddit.name)
        return res
    }

    suspend fun favorite(favorite: Boolean): Result<Unit> {
        val subreddit = info ?: return Result.failure(Exception("No info for subreddit"))
        val res = api.favorite(subreddit.displayName, favorite)
        return res
    }

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        val subreddit =
            currentSubreddit ?: return PagingSource.LoadResult.Page(emptyList(), null, null)
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
