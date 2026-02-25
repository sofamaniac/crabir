/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import android.util.Log
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.PostRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first
import retrofit2.Response

@Singleton
class SavedRepository @Inject constructor(
    postRepository: PostRepository,
    api: RedditAPIService,
    private val accountsRepository: AccountsRepository
) : FeedRepositoryCommon<FeedParams>(postRepository, api) {
    override suspend fun getPosts(
        after: String,
        params: FeedParams
    ): PagedResponse<String> {
        return makeRequest {
            val user = accountsRepository.activeAccount.first()
            if (user.isAnonymous()) {
                Log.w("SavedRepository", "getPosts: User is anonymous")
                return@makeRequest Response.success(null)
            }
            Log.d("SavedRepository", "getPosts: $user")
            api.getSaved(
                user = user.username,
                after = after,
            )
        }
    }

}
