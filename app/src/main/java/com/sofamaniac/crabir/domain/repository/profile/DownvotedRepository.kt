/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.repository.profile

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.feed.PostFeedRepository
import org.koin.core.annotation.ViewModelScope

@ViewModelScope
class DownvotedRepository(
    override val votableRepository: LinksRepository,
    val api: RedditAPIService,
) : PostFeedRepository<ProfileFeedParams>() {

    override suspend fun getThings(
        after: Fullname,
        params: ProfileFeedParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        if (params.username == RedditAccount.ANONYMOUS) return PagingSource.LoadResult.Page(
            emptyList(),
            null,
            null
        )
        return makeRequest {
            api.getDownvoted(
                user = params.username,
                after = after,
            )
        }
    }

}
