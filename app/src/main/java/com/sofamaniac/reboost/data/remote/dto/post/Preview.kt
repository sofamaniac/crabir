package com.sofamaniac.reboost.data.remote.dto.post

import com.sofamaniac.reboost.data.remote.utils.RedditVideoSerializer
import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    val images: List<PostImage> = emptyList(),
    @Serializable(with = RedditVideoSerializer::class)
    val redditVideoPreview: RedditVideo? = null,
    val enabled: Boolean? = false
)