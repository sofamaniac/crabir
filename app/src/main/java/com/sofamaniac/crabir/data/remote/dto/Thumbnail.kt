package com.sofamaniac.crabir.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Thumbnail(
    val uri: String,
    val width: Int,
    val height: Int,
)