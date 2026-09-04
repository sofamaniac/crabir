package com.sofamaniac.crabir.settings.api

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ApiSettings(
    val redditClientId: String? = null,
    val redditRedirectUri: String? = null,
) {
    val isConfigured: Boolean get() = !redditClientId.isNullOrBlank() && !redditRedirectUri.isNullOrBlank()
}

@Serializable
object ApiSettingsRoute : Route

val Context.apiSettingsDataStore by dataStore(
    fileName = "reboost_api_settings.json",
    serializer = DataStoreJsonSerializer(
        serializer = ApiSettings.serializer(),
        defaultValue = ApiSettings()
    )
)

@Composable
internal fun rememberApiSettings(): ApiSettings? {
    val context = LocalContext.current
    val settingsStore = remember(context) { context.apiSettingsDataStore }
    val settings by settingsStore.data.collectAsState(
        initial = null,
    )
    return settings
}

