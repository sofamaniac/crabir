package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.PostRepository
import jakarta.inject.Inject

class MultiPostsRepository @Inject constructor(
    postRepository: PostRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon(postRepository, api), FeedRepository {
    private var currentMulti: String? = null

    fun updateMulti(permalink: String) {
        currentMulti = permalink
    }

    override suspend fun getPosts(
        after: String,
        sort: Sort,
        timeframe: Timeframe?
    ): PagedResponse<String> {
        val subreddit = currentMulti ?: return PagedResponse()
        return makeRequest {
            api.getMulti(
                path = subreddit,
                after = after,
                sort = sort,
                timeframe = timeframe
            )
        }
    }
}