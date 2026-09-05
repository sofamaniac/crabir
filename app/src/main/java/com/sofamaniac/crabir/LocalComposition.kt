package com.sofamaniac.crabir

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.settings.api.ApiSettings
import com.sofamaniac.crabir.settings.comments.CommentsSettingsDefault
import com.sofamaniac.crabir.settings.data.DataSettingsDefault
import com.sofamaniac.crabir.settings.feedSettings.FeedSettings
import com.sofamaniac.crabir.settings.filters.FiltersSettings
import com.sofamaniac.crabir.settings.lateralMenu.LateralMenuSettingsDefault
import com.sofamaniac.crabir.settings.post.PostSettingsDefaults
import com.sofamaniac.crabir.settings.theme.ConfigureCrabirTheme
import com.sofamaniac.crabir.settings.theme.DefaultDarkTheme
import com.sofamaniac.crabir.settings.views.ViewSettings

val LocalTheme = compositionLocalOf { DefaultDarkTheme }
val LocalRedditAccount = compositionLocalOf { RedditAccount.anonymous() }
val LocalSnackBarHost = compositionLocalOf<SnackbarHostState?> { null }

val LocalViewSettings = compositionLocalOf { ViewSettings() }
val LocalFiltersSettings = compositionLocalOf { FiltersSettings() }
val LocalPostSettings = compositionLocalOf { PostSettingsDefaults.defaultPostSettings }
val LocalDataSettings = compositionLocalOf { DataSettingsDefault.defaultDataSettings }
val LocalCommentsSettings = compositionLocalOf { CommentsSettingsDefault.default }
val LocalLateralMenuSettings = compositionLocalOf { LateralMenuSettingsDefault.default }

val LocalFeedSettings = compositionLocalOf { FeedSettings() }
val LocalApiSettings = compositionLocalOf { ApiSettings() }

/**Setup local providers for previews*/
@Composable
fun PreviewLocalComposition(content: @Composable () -> Unit) {
    val redditAccount = RedditAccount.anonymous()
    val snackbarHostState = SnackbarHostState()
    ConfigureCrabirTheme {
        CompositionLocalProvider(
            LocalRedditAccount provides redditAccount,
            LocalSnackBarHost provides snackbarHostState
        ) {
            content()
        }
    }
}
