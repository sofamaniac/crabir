package com.sofamaniac.crabir.domain.repository.search

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.post.PostDataMapper
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.ListingRepository
import org.koin.core.annotation.ViewModelScope

@ViewModelScope
class PostSearchRepository(
    private val api: RedditAPIService,
    private val votableRepository: LinksRepository,
) :
    ListingRepository<PostSearchParams, PostData>() {
    override fun thingToData(thing: Thing): PostData? {
        if (thing !is Thing.Post) return null
        return PostDataMapper.map(thing.data)
    }

    override suspend fun onResponseSuccess(things: List<Thing>) {
        super.onResponseSuccess(things)
        val votableList = things.mapNotNull { thingToData(it) }
        votableRepository.insert(votableList)
    }

    override suspend fun getThings(
        after: Fullname,
        params: PostSearchParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        if (params.query.length < 3) return PagingSource.LoadResult.Page(emptyList(), null, null)
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

@ViewModelScope
class CommunitySearchRepository(private val api: RedditAPIService) :
    ListingRepository<CommunitySearchParams, SubredditData>() {
    override fun thingToData(thing: Thing): SubredditData? {
        if (thing !is Thing.Subreddit) return null
        return SubredditDTOMapper.map(thing.data)
    }

    override suspend fun getThings(
        after: Fullname,
        params: CommunitySearchParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        if (params.query.length < 3) return PagingSource.LoadResult.Page(emptyList(), null, null)
        return makeRequest {
            api.searchSubreddits(
                query = params.query,
                includeOver18 = params.includeOver18,
                exact = params.exact,
                after = after.name,
            )
        }
    }
}

@ViewModelScope
class UserSearchRepository(private val api: RedditAPIService) :
    ListingRepository<PostSearchParams, UserDTO>() {
    override fun thingToData(thing: Thing): UserDTO? {
        if (thing !is Thing.User) return null
        if (thing.data.id.isBlank()) return null
        return thing.data
    }

    override suspend fun getThings(
        after: Fullname,
        params: PostSearchParams,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        if (params.query.length < 3) return PagingSource.LoadResult.Page(emptyList(), null, null)
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