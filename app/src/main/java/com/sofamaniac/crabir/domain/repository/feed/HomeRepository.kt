/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.repository.LinksRepository
import org.koin.core.annotation.Singleton

@Singleton
class HomeRepository(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService
) : PostFeedRepository<FeedParams>() {

    override suspend fun getThings(
        after: Fullname,
        params: FeedParams
    ): PagedResponse<Fullname> {
        return makeRequest {
            api.getHome(
                sort = params.sort,
                timeframe = params.timeframe,
                after = after
            )
        }
    }
}
