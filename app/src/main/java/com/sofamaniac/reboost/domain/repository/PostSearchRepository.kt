package com.sofamaniac.reboost.domain.repository

import com.sofamaniac.reboost.data.remote.api.PostSearchSort
import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.feed.FeedRepositoryCommon

class PostSearchRepository(api: RedditAPIService, postRepository: PostRepository) :
    FeedRepositoryCommon<PostSearchParams>(api = api, postRepository = postRepository) {
    override suspend fun getPosts(
        after: String,
        params: PostSearchParams
    ): PagedResponse<String> {
        return makeRequest {
            if (params.subreddit == null) {
                api.searchPosts(
                    query = params.query,
                    sort = params.sort,
                    timeframe = params.timeframe,
                    after = after,
                )
            } else {
                api.searchPosts(
                    subreddit = params.subreddit,
                    restrictSubreddit = params.restrictSubreddit,
                    query = params.query,
                    sort = params.sort,
                    timeframe = params.timeframe,
                    after = after,
                )
            }
        }
    }

}

data class PostSearchParams(
    val query: String,
    val subreddit: String? = null,
    val restrictSubreddit: Boolean = true,
    val sort: PostSearchSort = PostSearchSort.Relevance,
    val timeframe: Timeframe? = null,
)