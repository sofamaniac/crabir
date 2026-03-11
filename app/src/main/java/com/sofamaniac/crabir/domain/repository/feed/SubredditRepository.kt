/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.repository.VotableRepository
import jakarta.inject.Inject

class SubredditPostsRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<FeedParams>(votableRepository, api) {
    private var currentSubreddit: String? = null

    fun updateSubreddit(subreddit: String) {
        currentSubreddit = subreddit
    }

    override suspend fun getThings(
        after: String,
        params: FeedParams
    ): PagedResponse<String> {
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
