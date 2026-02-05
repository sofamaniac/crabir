package com.sofamaniac.reboost.domain.model

data class MediaResource(val url: String, val aspectRatio: Float)


enum class Quality {
    Source,
    High,
    Medium,
    Low,
}