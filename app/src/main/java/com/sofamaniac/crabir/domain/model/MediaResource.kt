package com.sofamaniac.crabir.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R

data class MediaResource(val url: String, val aspectRatio: Float, val width: Int, val height: Int) {
    val hasValidAspectRatio: Boolean = aspectRatio > 0f
}


enum class Quality {
    Source,
    High,
    Medium,
    Low,
}

@Composable
fun Quality.stringResource(): String {
    return stringResource(
        when (this) {
            Quality.Source -> R.string.quality_source
            Quality.High -> R.string.quality_high
            Quality.Medium -> R.string.quality_medium
            Quality.Low -> R.string.quality_low
        }
    )
}
