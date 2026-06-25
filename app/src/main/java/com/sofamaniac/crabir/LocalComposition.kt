package com.sofamaniac.crabir

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.settings.theme.CrabirTheme
import com.sofamaniac.crabir.settings.theme.DefaultDarkTheme

val LocalTheme = compositionLocalOf<CrabirTheme> { DefaultDarkTheme }
val LocalDrawerState = compositionLocalOf<DrawerState> { error("No drawer state provided") }
val LocalRedditAccount = compositionLocalOf<RedditAccount> { RedditAccount.anonymous() }
val LocalSharedTransitionScope =
    compositionLocalOf<SharedTransitionScope> { error("No shared transition scope provided") }

val LocalSnackBarHost = compositionLocalOf<SnackbarHostState?> { null }