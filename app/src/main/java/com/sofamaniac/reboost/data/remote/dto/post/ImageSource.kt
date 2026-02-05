/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.dto.post

import com.sofamaniac.reboost.domain.model.MediaResource
import kotlinx.serialization.Serializable

@Serializable
data class PostImageSource(
    val url: String,
    val width: Int = 0,
    val height: Int = 1,
) {
    fun toMediaResource() = MediaResource(url, width.toFloat() / height.toFloat())
}
