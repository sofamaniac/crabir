/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class PostImage(
    val source: PostImageSource,
    val resolutions: List<PostImageSource>,
    val id: String,
    val variants: PostImageVariants? = null,
)

@Serializable
data class PostImageVariants(
    val obfuscated: PostImageData? = null,
    val nsfw: PostImageData? = null,
    val gif: PostImageData? = null,
    val mp4: PostImageData? = null,
)

@Serializable
data class PostImageData(
    val source: PostImageSource,
    val resolutions: List<PostImageSource> = emptyList(),
)