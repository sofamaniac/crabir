/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.reddit

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val uri: String,
    val width: Int,
    val height: Int
)