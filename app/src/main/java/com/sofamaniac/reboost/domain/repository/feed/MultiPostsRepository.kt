package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.PostRepository
import jakarta.inject.Inject

class MultiPostsRepository @Inject constructor(
    postRepository: PostRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<FeedParams>(postRepository, api) {
    private var currentMulti: String? = null

    fun updateMulti(permalink: String) {
        currentMulti = permalink
    }

    override suspend fun getPosts(
        after: String,
        params: FeedParams,
    ): PagedResponse<String> {
        val subreddit = currentMulti ?: return PagedResponse()
        return makeRequest {
            api.getMultreddit(
                path = subreddit,
                after = after,
                sort = params.sort,
                timeframe = params.timeframe
            )
        }
    }
}