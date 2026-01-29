/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.domain.repository.feed

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    postRepository: PostRepository,
    api: RedditAPIService
) : FeedRepositoryCommon(postRepository, api), FeedRepository {

    override suspend fun getPosts(
        after: String,
        sort: Sort,
        timeframe: Timeframe?
    ): PagedResponse<String> {
        return makeRequest {
            api.getHome(
                sort = sort,
                timeframe = timeframe,
                after = after
            )
        }
    }
}
