/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import java.time.Clock
import java.time.Duration

const val DAYS_IN_YEAR = 365
const val DAYS_IN_MONTH = 30

@Composable
fun formatElapsedTimeLocalized(
    creationDate: kotlin.time.Instant,
): String {
    val end = Clock.systemUTC().millis()
    val duration = Duration.ofMillis(kotlin.math.abs(end - creationDate.toEpochMilliseconds()))

    return when {
        duration.toDays() >= DAYS_IN_YEAR -> stringResource(
            R.string.elapsed_year,
            duration.toDays() / DAYS_IN_YEAR
        )

        duration.toDays() >= DAYS_IN_MONTH -> stringResource(
            R.string.elapsed_month,
            duration.toDays() / DAYS_IN_MONTH
        )

        duration.toDays() > 0 -> stringResource(R.string.elapsed_day, duration.toDays())
        duration.toHours() > 0 -> stringResource(R.string.elapsed_hour, duration.toHours())
        duration.toMinutes() > 0 -> stringResource(R.string.elapsed_minute, duration.toMinutes())
        else -> stringResource(R.string.elapsed_second, duration.seconds)
    }
}

/** Make composable clickable while preventing touch event in children */
fun Modifier.protectedTouch(enabled: Boolean = true, onClick: () -> Unit): Modifier {
    val pass = PointerEventPass.Initial
    if (!enabled) return this
    return this.then(
        Modifier.pointerInput(pass) {
            awaitEachGesture {
                val down = awaitFirstDown(pass = pass)
                down.consume()
                val up = waitForUpOrCancellation(pass = pass)
                if (up != null) {
                    onClick()
                }
            }
        }
    )
}

