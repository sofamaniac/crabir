package com.sofamaniac.crabir.domain.repository.feed

import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.repository.LinksRepository
import jakarta.inject.Inject

class MultiPostsRepository @Inject constructor(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService,
) : PostFeedRepository<FeedParams>() {
    private var currentMulti: String? = null

    fun updateMulti(permalink: String) {
        currentMulti = permalink
    }

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams,
    ): PagedResponse<Fullname> {
        val subreddit = currentMulti ?: return PagedResponse()
        return makeRequest {
            api.getMultireddit(
                path = subreddit,
                after = after,
                sort = params.sort,
                timeframe = params.timeframe
            )
        }
    }
}