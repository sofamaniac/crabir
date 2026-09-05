/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.model

import androidx.annotation.Keep
import com.sofamaniac.crabir.data.remote.dto.post.PostDTO
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class Kind {
    Self,
    Image,
    Video,
    Gallery,
    Meta,
    Link,
    YoutubeVideo,
    Streamable,
    Unknown,
}

fun isVideoPost(post: PostDTO): Boolean {
    return post.isVideo || (post.postHint == "image" && isVideoUrl(post.url)) ||
            (post.preview?.images?.any { it.variants?.mp4 != null } ?: false) ||
            (post.preview?.redditVideoPreview != null)
}

fun isStreamable(post: PostDTO): Boolean {
    return post.secureMedia?.type == "streamable.com"
}

fun isYoutubeVideo(post: PostDTO): Boolean {
    return post.domain.contains("youtube") || post.domain.contains("youtu.be")
}

fun getKind(post: PostDTO): Kind {
    if (post.crosspostParentList.isNotEmpty()) return getKind(post.crosspostParentList.first())

    val kind = if (isStreamable(post)) {
        Kind.Streamable
    } else if (isVideoPost(post)) {
        Kind.Video
    } else if (isYoutubeVideo(post)) {
        Kind.YoutubeVideo
    } else if (post.isSelfPost) {
        Kind.Self
    } else if (post.isVideo) {
        Kind.Video
    } else if (post.isGallery || post.galleryData != null) {
        Kind.Gallery
    } else if (post.isMeta) {
        Kind.Meta
    } else {
        null
    }

    if (kind != null) return kind

    return when (post.postHint) {
        "image" -> Kind.Image
        "rich:video", "hosted:video" -> Kind.Video
        "link" -> Kind.Link
        else -> {
            if (post.url.startsWith("https://www.reddit.com/gallery")) {
                Kind.Gallery
            } else if (isVideoUrl(post.url)) {
                Kind.Video
            } else if (isImageUrl(post.url)) {
                Kind.Image
            } else {
                Kind.Unknown
            }
        }
    }
}

fun isVideoUrl(url: String): Boolean {
    val extensions = listOf("gif", "mp4")
    return extensions.any { url.endsWith(it) }
}

fun isImageUrl(url: String): Boolean {
    val extensions = listOf("jpg", "png", "jpeg", "png", "svg")
    return extensions.any { url.endsWith(it) }
}
