package com.sofamaniac.crabir.domain.model

data class MediaResource(val url: String, val aspectRatio: Float, val width: Int, val height: Int) {
    val hasValidAspectRatio: Boolean = aspectRatio > 0f
}


enum class Quality {
    Source,
    High,
    Medium,
    Low,
}