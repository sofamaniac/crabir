/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.profile

import android.util.Log
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.VotableRepository
import com.sofamaniac.crabir.domain.repository.feed.FeedRepositoryCommon
import jakarta.inject.Inject
import jakarta.inject.Singleton
import retrofit2.Response

@Singleton
class SavedRepository @Inject constructor(
    votableRepository: VotableRepository,
    api: RedditAPIService,
) : FeedRepositoryCommon<ProfileFeedParams>(votableRepository, api) {
    override suspend fun getThings(
        after: String,
        params: ProfileFeedParams
    ): PagedResponse<String> {
        return makeRequest {
            if (params.username == RedditAccount.ANONYMOUS) {
                Log.w("SavedRepository", "getPosts: User is anonymous")
                return@makeRequest Response.success(null)
            }
            Log.d("SavedRepository", "getPosts: ${params.username}")
            api.getSaved(
                user = params.username,
                after = after,
            )
        }
    }

}
