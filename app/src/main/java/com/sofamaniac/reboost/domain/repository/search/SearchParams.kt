package com.sofamaniac.reboost.domain.repository.search

import com.sofamaniac.reboost.data.remote.api.SearchSort
import com.sofamaniac.reboost.data.remote.dto.Timeframe

data class SearchParams(
    val query: String,
    val subreddit: String? = null,
    val restrictSubreddit: Boolean = true,
    /** Either "link", "sr", or "user" */
    val type: String,
    val sort: SearchSort,
    val timeframe: Timeframe? = null,
)