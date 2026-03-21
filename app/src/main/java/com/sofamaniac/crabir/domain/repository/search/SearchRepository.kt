package com.sofamaniac.crabir.domain.repository.search

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDetailsMapper
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PagedResponse
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.ListingRepository

class PostSearchRepository(private val api: RedditAPIService) :
    ListingRepository<PostSearchParams, PostData>() {
    override fun thingToData(thing: Thing): PostData? {
        if (thing !is Thing.Post) return null
        return PostDataMapper.map(thing.data)
    }

    override suspend fun getThings(
        after: Fullname,
        params: PostSearchParams
    ): PagedResponse<Fullname> {
        if (params.query.length < 3) return PagedResponse()
        return makeRequest {
            api.search(
                subreddit = params.subreddit ?: "all",
                restrictSubreddit = params.restrictSubreddit,
                query = params.query,
                sort = params.sort,
                timeframe = params.timeframe,
                after = after,
                type = "link"
            )
        }
    }
}

class CommunitySearchRepository(private val api: RedditAPIService) :
    ListingRepository<CommunitySearchParams, SubredditData>() {
    override fun thingToData(thing: Thing): SubredditData? {
        if (thing !is Thing.Subreddit) return null
        return SubredditDetailsMapper.map(thing.data)
    }

    override suspend fun getThings(
        after: Fullname,
        params: CommunitySearchParams
    ): PagedResponse<Fullname> {
        if (params.query.length < 3) return PagedResponse()
        return makeRequest {
            api.search(
                after = after,
                query = params.query,
                type = "sr",
                sort = params.sort,
                timeframe = params.timeframe,
            )
        }
    }
}

class UserSearchRepository(private val api: RedditAPIService) :
    ListingRepository<PostSearchParams, UserDTO>() {
    override fun thingToData(thing: Thing): UserDTO? {
        if (thing !is Thing.User) return null
        if (thing.data.id.isBlank()) return null
        return thing.data
    }

    override suspend fun getThings(
        after: Fullname,
        params: PostSearchParams
    ): PagedResponse<Fullname> {
        if (params.query.length < 3) return PagedResponse()
        return makeRequest {
            api.search(
                subreddit = params.subreddit ?: "all",
                restrictSubreddit = params.restrictSubreddit,
                query = params.query,
                sort = params.sort,
                timeframe = params.timeframe,
                after = after,
                type = "user"
            )
        }
    }
}

//class CommentSearchRepository(api: RedditAPIService) : SearchRepositoryGeneric<CommentData>(api) {
//    override fun thingToData(thing: Thing): CommentData? {
//        if (thing !is Thing.Comment) return null
//        return CommentDataMapper.map(thing.data)
//    }
//}