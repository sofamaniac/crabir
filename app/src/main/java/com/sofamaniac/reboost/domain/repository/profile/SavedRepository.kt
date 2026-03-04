/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.profile

import android.util.Log
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.model.RedditAccount
import com.sofamaniac.reboost.domain.repository.VotableRepository
import com.sofamaniac.reboost.domain.repository.feed.FeedRepositoryCommon
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
