/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.profile

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.domain.repository.VotableRepository
import com.sofamaniac.reboost.domain.repository.feed.FeedRepositoryCommon
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class SubmittedRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<ProfileFeedParams>(votableRepository, api) {
    override suspend fun getThings(
        after: String,
        params: ProfileFeedParams
    ): PagedResponse<String> {
        if (params.username == RedditAccount.ANONYMOUS) return PagedResponse()
        return makeRequest {
            api.getSubmitted(
                user = params.username,
                after = after,
                sort = params.sort,
                timeframe = params.timeframe,
            )
        }
    }

}
