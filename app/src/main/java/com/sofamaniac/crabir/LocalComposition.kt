package com.sofamaniac.crabir

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.settings.theme.CrabirTheme
import com.sofamaniac.crabir.settings.theme.DefaultDarkTheme

val LocalTheme = compositionLocalOf<CrabirTheme> { DefaultDarkTheme }
val LocalDrawerState = compositionLocalOf<DrawerState> { error("No drawer state provided") }
val LocalRedditAccount = compositionLocalOf<RedditAccount> { RedditAccount.anonymous() }
val LocalSnackBarHost = compositionLocalOf<SnackbarHostState?> { null }

/**Setup local providers for previews*/
@Composable
fun PreviewLocalComposition(content: @Composable () -> Unit) {
    val drawerState = DrawerState(DrawerValue.Closed)
    val redditAccount = RedditAccount.anonymous()
    val snackbarHostState = SnackbarHostState()
    CompositionLocalProvider(
        LocalTheme provides DefaultDarkTheme,
        LocalDrawerState provides drawerState,
        LocalRedditAccount provides redditAccount,
        LocalSnackBarHost provides snackbarHostState
    ) {
        content()
    }
}