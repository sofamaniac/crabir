/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditId
import kotlinx.serialization.Serializable

@Serializable
data class SubredditInfo(
    val name: String,
    val subredditId: SubredditId,
    val subredditPrefixed: String,
    val subredditSubscribers: Int,
    val subredditType: String
)