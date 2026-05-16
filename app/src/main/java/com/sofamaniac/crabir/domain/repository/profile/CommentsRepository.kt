/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.profile

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.CommentsRepository
import com.sofamaniac.crabir.domain.repository.feed.CommentFeedRepository
import jakarta.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentsRepository @Inject constructor(
    override val votableRepository: CommentsRepository,
    val api: RedditAPIService,
) : CommentFeedRepository<ProfileFeedParams>() {
    override suspend fun getThings(
        after: Fullname,
        params: ProfileFeedParams,
    ): PagedResponse<Fullname> {
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
