/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.feed

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.repository.LinksRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
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
