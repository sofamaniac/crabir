/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.VotableRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class UpvotedRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService,
    private val accountsRepository: AccountsRepository
) : FeedRepositoryCommon<FeedParams>(votableRepository, api) {
    override suspend fun getThings(
        after: String,
        params: FeedParams
    ): PagedResponse<String> {
        val user = accountsRepository.activeAccount.first()
        if (user.isAnonymous()) return PagedResponse()
        return makeRequest {
            api.getUpvoted(
                user = user.username,
                after = after,
            )
        }
    }

}
