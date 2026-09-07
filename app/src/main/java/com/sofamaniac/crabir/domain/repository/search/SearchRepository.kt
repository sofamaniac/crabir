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
import com.sofamaniac.crabir.domain.repository.Cache
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.ListingRepository
import com.sofamaniac.crabir.domain.repository.feed.SubredditCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Singleton
import org.koin.core.annotation.ViewModelScope

@ViewModelScope
class PostSearchRepository(
    private val api: RedditAPIService,
    override val cache: LinksRepository,
) :
    ListingRepository<PostSearchParams, PostData>() {
    override fun thingToData(thing: Thing): PostData? {
        if (thing !is Thing.Post) return null
        return PostDataMapper.map(thing.data)
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
class CommunitySearchRepository(
    private val api: RedditAPIService,
    override val cache: SubredditCache,
) :
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

@Singleton
class UserCache : Cache<UserDTO> {
    private val cache = mutableMapOf<Fullname, UserDTO>()
    override fun insert(thing: UserDTO) {
        cache[thing.name] = thing
    }

    override fun insert(things: List<UserDTO>) {
        things.forEach { insert(it) }
    }

    override fun update(thing: UserDTO) {
        cache[thing.name] = thing
    }

    override fun get(name: Fullname): Flow<UserDTO?> {
        return flowOf(cache[name])
    }

    override suspend fun getAsync(name: Fullname): UserDTO? {
        return cache[name]
    }

    override suspend fun delete(name: Fullname) {
        cache.remove(name)
    }

    override fun clear() {
        cache.clear()
    }
}

@ViewModelScope
class UserSearchRepository(private val api: RedditAPIService, override val cache: UserCache) :
    ListingRepository<PostSearchParams, UserDTO>() {
    override fun thingToData(thing: Thing): UserDTO? {
        return if (thing !is Thing.User) {
            null
        } else if (thing.data.id.isBlank()) {
            null
        } else {
            thing.data
        }
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
