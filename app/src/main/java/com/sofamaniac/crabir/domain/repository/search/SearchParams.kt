package com.sofamaniac.crabir.domain.repository.search

import com.sofamaniac.crabir.data.remote.api.SearchSort
import com.sofamaniac.crabir.data.remote.dto.Timeframe

data class SearchParams(
    val query: String,
    val subreddit: String? = null,
    val restrictSubreddit: Boolean = true,
    /** Either "link", "sr", or "user" */
    val type: String,
    val sort: SearchSort,
    val timeframe: Timeframe? = null,
)