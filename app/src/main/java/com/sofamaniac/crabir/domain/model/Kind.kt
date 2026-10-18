/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.domain.model

import androidx.annotation.Keep
import com.sofamaniac.crabir.data.remote.dto.post.PostDTO
import com.sofamaniac.crabir.isImageUrl
import com.sofamaniac.crabir.isVideoUrl
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

fun getKind(post: PostDTO): Kind {
    if (post.crosspostParentList.isNotEmpty()) return getKind(post.crosspostParentList.first())

    return when {
        post.isSelfPost -> {
            Kind.Self
        }

        post.isVideo -> {
            Kind.Video
        }

        post.isGallery || post.galleryData != null -> {
            Kind.Gallery
        }

        post.isMeta -> {
            Kind.Meta
        }

        isStreamable(post) -> {
            Kind.Streamable
        }

        isVideoPost(post) -> {
            Kind.Video
        }

        isYoutubeVideo(post) -> {
            Kind.YoutubeVideo
        }

        else -> {
            kindFromHint(post)
        }
    }
}

private fun isVideoPost(post: PostDTO): Boolean {
    return post.isVideo || (post.postHint == "image" && isVideoUrl(post.url)) ||
            (post.preview?.images?.any { it.variants?.mp4 != null } ?: false) ||
            (post.preview?.redditVideoPreview != null)
}

private fun isStreamable(post: PostDTO): Boolean {
    return post.secureMedia?.type == "streamable.com"
}

private fun isYoutubeVideo(post: PostDTO): Boolean {
    return post.domain.contains("youtube") || post.domain.contains("youtu.be")
}

private fun kindFromHint(post: PostDTO): Kind {
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


