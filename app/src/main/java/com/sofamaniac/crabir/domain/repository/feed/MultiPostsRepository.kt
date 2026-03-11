package com.sofamaniac.crabir.domain.repository.feed

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.repository.VotableRepository
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