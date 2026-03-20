package com.sofamaniac.crabir.domain.repository.search

import com.sofamaniac.crabir.data.remote.api.PostSearchSort
import com.sofamaniac.crabir.data.remote.api.SearchSort
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.ui.search.SearchParams

data class PostSearchParams(
    override val query: String,
    val subreddit: String? = null,
    val restrictSubreddit: Boolean = true,
    /** Either "link", "sr", or "user" */
    val type: String,
    val sort: PostSearchSort,
    val timeframe: Timeframe? = null,
) : SearchParams<PostSearchParams> {
    override fun copy(query: String): PostSearchParams {
        return copy(query = query, type = type)
    }
}

data class CommunitySearchParams(
    override val query: String,
    val sort: SearchSort,
    val timeframe: Timeframe? = null,
    val includeOver18: Boolean = false,
    val exact: Boolean = false,
) : SearchParams<CommunitySearchParams> {
    override fun copy(query: String): CommunitySearchParams {
        return copy(query = query, exact = exact)
    }
}
