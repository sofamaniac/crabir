/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.VotableRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService
) : FeedRepositoryCommon<FeedParams>(votableRepository, api) {

    override suspend fun getThings(
        after: String,
        params: FeedParams
    ): PagedResponse<String> {
        return makeRequest {
            api.getHome(
                sort = params.sort,
                timeframe = params.timeframe,
                after = after
            )
        }
    }
}
