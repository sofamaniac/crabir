/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.remote.dto.post.Media
import kotlinx.serialization.Serializable

@Serializable
data class MediaInfo(
    val media: Media? = null,
    val mediaEmbed: Map<String, String> = emptyMap(),
    val mediaOnly: Boolean = false,
)

