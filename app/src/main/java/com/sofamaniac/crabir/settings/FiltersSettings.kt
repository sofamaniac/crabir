package com.sofamaniac.crabir.settings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable

@Serializable
data class FiltersSettings(
    val showNSFW: Boolean = true,
    val showNSFWMedia: Boolean = true,
    val blurNSFW: Boolean = false,
)

val Context.filtersDataStore by dataStore(
    fileName = "reboost_filters.json",
    serializer = DataStoreJsonSerializer(
        serializer = FiltersSettings.serializer(),
        defaultValue = FiltersSettings()
    )
)

@Composable
fun rememberFiltersSettings(): FiltersSettings {
    val context = LocalContext.current
    val filtersSettingsDataStore = remember(context) { context.filtersDataStore }
    val filtersSettings by filtersSettingsDataStore.data.collectAsState(
        initial = FiltersSettings(),
    )
    return filtersSettings
}

