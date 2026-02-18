package com.sofamaniac.reboost.domain.model

data class MediaResource(val url: String, val aspectRatio: Float, val width: Int, val height: Int)


enum class Quality {
    Source,
    High,
    Medium,
    Low,
}