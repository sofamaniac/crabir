/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.PostRepository
import jakarta.inject.Inject

class SubredditPostsRepository @Inject constructor(
    postRepository: PostRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<FeedParams>(postRepository, api) {
    private var currentSubreddit: String? = null

    fun updateSubreddit(subreddit: String) {
        currentSubreddit = subreddit
    }

    override suspend fun getPosts(
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
