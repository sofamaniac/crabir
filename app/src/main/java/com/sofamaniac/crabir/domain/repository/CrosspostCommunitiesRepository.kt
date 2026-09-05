package com.sofamaniac.crabir.domain.repository

import androidx.paging.PagingSource
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTOMapper
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.SubredditData
import org.koin.core.annotation.ViewModelScope

@ViewModelScope
class CrosspostCommunitiesRepository(
    val api: RedditAPIService,
) : ListingRepository<RedditAccount?, SubredditData>() {
    override fun thingToData(thing: Thing): SubredditData? {
        return when (thing) {
            is Thing.Subreddit -> {
                SubredditDTOMapper.map(thing.data)
            }

            else -> null
        }
    }

    override suspend fun getThings(
        after: Fullname,
        params: RedditAccount?,
    ): PagingSource.LoadResult<Fullname, Fullname> {
        return makeRequest { api.getCrosspostableSubreddits(account = params, after = after) }

    }

}
