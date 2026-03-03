package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.VotableRepository
import jakarta.inject.Inject

class MultiPostsRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<FeedParams>(votableRepository, api) {
    private var currentMulti: String? = null

    fun updateMulti(permalink: String) {
        currentMulti = permalink
    }

    override suspend fun getThings(
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