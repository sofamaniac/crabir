package com.sofamaniac.reboost.domain.repository.search

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.comment.CommentDataMapper
import com.sofamaniac.reboost.data.remote.dto.post.PostDataMapper
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditData
import com.sofamaniac.reboost.data.remote.dto.user.UserDTO
import com.sofamaniac.reboost.domain.model.CommentData
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.repository.DataInterface
import com.sofamaniac.reboost.domain.repository.ListingRepository

abstract class SearchRepositoryGeneric<Data : DataInterface>(private val api: RedditAPIService) :
    ListingRepository<SearchParams, Data>() {

    override suspend fun getThings(after: String, params: SearchParams): PagedResponse<String> {
        return makeRequest {
            api.search(
                subreddit = params.subreddit ?: "all",
                restrictSubreddit = params.restrictSubreddit,
                query = params.query,
                sort = params.sort,
                timeframe = params.timeframe,
                after = after,
                type = params.type
            )
        }
    }
}

class PostSearchRepository(api: RedditAPIService) : SearchRepositoryGeneric<PostData>(api) {
    override fun thingToData(thing: Thing): PostData? {
        if (thing !is Thing.Post) return null
        return PostDataMapper.map(thing.data)
    }
}

class CommunitySearchRepository(api: RedditAPIService) :
    SearchRepositoryGeneric<SubredditData>(api) {
    override fun thingToData(thing: Thing): SubredditData? {
        if (thing !is Thing.Subreddit) return null
        return thing.data
    }
}

class UserSearchRepository(api: RedditAPIService) : SearchRepositoryGeneric<UserDTO>(api) {
    override fun thingToData(thing: Thing): UserDTO? {
        if (thing !is Thing.User) return null
        return thing.data
    }
}

class CommentSearchRepository(api: RedditAPIService) : SearchRepositoryGeneric<CommentData>(api) {
    override fun thingToData(thing: Thing): CommentData? {
        if (thing !is Thing.Comment) return null
        return CommentDataMapper.map(thing.data)
    }
}