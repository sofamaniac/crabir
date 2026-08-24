package com.sofamaniac.crabir.data.remote.streamable

import com.sofamaniac.crabir.domain.model.MediaResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamableAnswer(
    val status: Int,
    val percent: Int,
    val url: String,
    val files: Files,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String
)

@Serializable
data class Files(
    @SerialName("mp4-mobile")
    val mp4Mobile: Video,
    val mp4: Video,
    val original: Video,
)

@Serializable
data class Video(
    val status: Int? = null,
    val url: String? = null,
    val framerate: Double,
    val width: Int,
    val height: Int,
    val bitrate: Int,
    val size: Int,
    val duration: Double,
) {
    fun toMediaResource(): MediaResource {
        return MediaResource(
            url = url!!,
            width = width,
            height = height,
            aspectRatio = width.toFloat() / height.coerceAtLeast(1).toFloat()
        )
    }
}