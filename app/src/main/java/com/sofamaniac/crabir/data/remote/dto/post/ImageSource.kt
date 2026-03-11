/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.dto.post

import com.sofamaniac.crabir.domain.model.MediaResource
import kotlinx.serialization.Serializable

@Serializable
data class PostImageSource(
    val url: String,
    val width: Int = 0,
    val height: Int = 1,
) {
    fun toMediaResource() = MediaResource(url, width.toFloat() / height.toFloat(), width, height)
}
