/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.profile

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.feed.PostFeedRepository
import jakarta.inject.Inject

class HiddenRepository @Inject constructor(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService,
) : PostFeedRepository<ProfileFeedParams>() {
    override suspend fun getThings(
        after: Fullname,
        params: ProfileFeedParams
    ): PagedResponse<Fullname> {
        if (params.username == RedditAccount.ANONYMOUS) return PagedResponse()
        return makeRequest {
            api.getHidden(
                user = params.username,
                after = after,
            )
        }
    }

}
