/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.profile

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.VotableRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedRepositoryCommon
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentsRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<ProfileFeedParams>(votableRepository, api) {
    override suspend fun getThings(
        after: String,
        params: ProfileFeedParams,
    ): PagedResponse<String> {
        if (params.username == RedditAccount.ANONYMOUS) return PagedResponse()

        return makeRequest {
            api.getComments(
                user = params.username,
                after = after,
                sort = params.sort,
                timeframe = params.timeframe,
            )
        }
    }
}
