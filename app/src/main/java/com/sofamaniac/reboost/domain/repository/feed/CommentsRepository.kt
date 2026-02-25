/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.PostRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class CommentsRepository @Inject constructor(
    postRepository: PostRepository,
    api: RedditAPIService,
    private val accountsRepository: AccountsRepository
) : FeedRepositoryCommon<FeedParams>(postRepository, api) {
    override suspend fun getPosts(
        after: String,
        params: FeedParams,
    ): PagedResponse<String> {
        val user = accountsRepository.activeAccount.first()
        if (user.isAnonymous()) return PagedResponse()
        return makeRequest {
            api.getComments(
                user = user.username,
                after = after,
            )
        }
    }
}
