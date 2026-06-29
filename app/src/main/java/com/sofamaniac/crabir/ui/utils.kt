/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui

import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.domain.model.Fullname
import dev.chrisbanes.haze.blur.HazeBlurStyle
import java.time.Clock
import java.time.Duration
import java.util.Locale

fun formatElapsedTimeLocalized(
    creationDate: kotlin.time.Instant,
    locale: Locale = Locale.getDefault()
): String {
    val end = Clock.systemUTC().millis()
    val duration = Duration.ofMillis(kotlin.math.abs(end - creationDate.toEpochMilliseconds()))

    return when {
        duration.toDays() >= 365 -> String.format(locale, "%dy", duration.toDays() / 365)
        duration.toDays() >= 30 -> String.format(locale, "%dmo", duration.toDays() / 30)
        duration.toDays() > 0 -> String.format(locale, "%dd", duration.toDays())
        duration.toHours() > 0 -> String.format(locale, "%dh", duration.toHours())
        duration.toMinutes() > 0 -> String.format(locale, "%dm", duration.toMinutes())
        else -> String.format(locale, "%ds", duration.seconds)
    }
}

enum class SharedElementType {
    Post,
    Content,
}

data class SharedElementKey(val name: Fullname, val type: SharedElementType)

fun crabirBlurStyle(): HazeBlurStyle {
    return HazeBlurStyle(blurRadius = 40.dp, colorEffects = null, noiseFactor = 0f)
}