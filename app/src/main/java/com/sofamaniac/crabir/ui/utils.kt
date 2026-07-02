/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import dev.chrisbanes.haze.blur.HazeBlurStyle
import net.openid.appauth.AuthState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel
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

@Composable
fun rememberCurrentAccount(): RedditAccount {
    val viewModel: CurrentAccountViewModel = koinViewModel()
    val account by viewModel.account.collectAsState(RedditAccount.uninitialized(-2, AuthState()))
    return account
}

@KoinViewModel
class CurrentAccountViewModel(accountsRepository: AccountsRepository) : ViewModel() {
    val account = accountsRepository.activeAccount
}
