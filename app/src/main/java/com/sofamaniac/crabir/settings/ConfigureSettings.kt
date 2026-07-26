package com.sofamaniac.crabir.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.sofamaniac.crabir.LocalDataSettings
import com.sofamaniac.crabir.LocalFiltersSettings
import com.sofamaniac.crabir.LocalPostSettings
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.settings.data.rememberDataSettings
import com.sofamaniac.crabir.settings.filters.rememberFiltersSettings
import com.sofamaniac.crabir.settings.post.rememberPostsSettings
import com.sofamaniac.crabir.settings.views.rememberViewSettings

@Composable
fun ConfigureSettings(content: @Composable () -> Unit) {
    val postSettings = rememberPostsSettings() ?: return
    val filtersSettings = rememberFiltersSettings() ?: return
    val viewSettings = rememberViewSettings() ?: return
    val dataSettings = rememberDataSettings() ?: return

    CompositionLocalProvider(
        LocalPostSettings provides postSettings,
        LocalFiltersSettings provides filtersSettings,
        LocalViewSettings provides viewSettings,
        LocalDataSettings provides dataSettings,
    ) {
        content()
    }
}