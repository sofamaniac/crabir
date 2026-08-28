/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.profile

import android.util.Log
import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.MixedRepository
import com.sofamaniac.crabir.domain.repository.feed.MixedFeedRepository
import org.koin.core.annotation.ViewModelScope

@ViewModelScope
class SavedRepository(
    override val votableRepository: MixedRepository,
    val api: RedditAPIService,
) : MixedFeedRepository<ProfileFeedParams>() {
    override suspend fun getThings(
        after: Fullname,
        params: ProfileFeedParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        return makeRequest {
            if (params.username == RedditAccount.ANONYMOUS) {
                Log.w("SavedRepository", "getPosts: User is anonymous")
                return@makeRequest Result.failure(Exception("User is anonymous"))
            }
            Log.d("SavedRepository", "getPosts: ${params.username}")
            api.getSaved(
                user = params.username,
                after = after,
            )
        }
    }
}
