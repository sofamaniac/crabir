package com.sofamaniac.crabir.data.remote.dto.post

import com.sofamaniac.crabir.data.remote.utils.RedditVideoSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    val images: List<PostImage> = emptyList(),
    @SerialName("reddit_video_preview")
    @Serializable(with = RedditVideoSerializer::class)
    val redditVideoPreview: RedditVideo? = null,
    val enabled: Boolean? = false,
)
