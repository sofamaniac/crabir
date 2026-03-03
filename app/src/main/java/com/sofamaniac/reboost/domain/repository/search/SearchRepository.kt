package com.sofamaniac.reboost.domain.repository.search

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.domain.model.PagedResponse
import com.sofamaniac.reboost.domain.repository.ListingRepository

class SearchRepository(private val api: RedditAPIService) :
    ListingRepository<SearchParams, Thing>() {

    override fun thingToData(thing: Thing): Thing {
        return thing
    }

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