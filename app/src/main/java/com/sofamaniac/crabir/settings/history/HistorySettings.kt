package com.sofamaniac.crabir.settings.history

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import kotlinx.serialization.Serializable

@Serializable
data class HistorySettings(
    val enabled: Boolean = true,
    val saveNSFW: Boolean = true,
    val readOnScroll: Boolean = false,
    val dimImages: Boolean = false,
)

val Context.historySettingsDataStore by dataStore(
    fileName = "reboost_history_settings.json",
    serializer = DataStoreJsonSerializer(
        serializer = HistorySettings.serializer(),
        defaultValue = HistorySettings()
    )
)

@Composable
internal fun rememberHistorySettings(): HistorySettings? {
    val context = LocalContext.current
    val settingsStore = remember(context) { context.historySettingsDataStore }
    val settings by settingsStore.data.collectAsState(
        initial = null,
    )
    return settings
}

