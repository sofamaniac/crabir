/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.PostRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class HiddenRepository @Inject constructor(
    postRepository: PostRepository,
    api: RedditAPIService,
    private val accountsRepository: AccountsRepository
) : FeedRepositoryCommon(postRepository, api), FeedRepository {
    override suspend fun getPosts(
        after: String,
        sort: Sort,
        timeframe: Timeframe?
    ): PagedResponse<String> {
        val user = accountsRepository.activeAccount.first()
        if (user.isAnonymous()) return PagedResponse()
        return makeRequest {
            api.getHidden(
                user = user.username,
                after = after,
            )
        }
    }

}
