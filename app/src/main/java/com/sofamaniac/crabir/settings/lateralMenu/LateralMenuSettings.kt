package com.sofamaniac.crabir.settings.lateralMenu

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
data class LateralMenuSettings(
    val items: LateralMenuItems = LateralMenuItems(),
    val showSubscriptions: Boolean = true,
    val showIcons: Boolean = true,
    val showFavOnly: Boolean = false,
)

@Serializable
data class LateralMenuItems(
    val defaultFeed: Boolean = true,
    val homeFeed: Boolean = false,
    val popular: Boolean = true,
    val all: Boolean = true,
    val saved: Boolean = true,
    val history: Boolean = true,
    val profile: Boolean = true,
    val inbox: Boolean = true,
    val friends: Boolean = false,
    val drafts: Boolean = false,
    val moderation: Boolean = false,
    val search: Boolean = false,
    val goToMenu: Boolean = true,
    val goToCommunity: Boolean = false,
    val goToUser: Boolean = false,
    val darkMode: Boolean = false,
    val blurNSFW: Boolean = false,
    val showNSFW: Boolean = false,
)

object LateralMenuSettingsDefault {
    val default = LateralMenuSettings()
}

@Serializable
object LateralMenuSettingsRoute : Route

val Context.lateralMenuSettingsDataStore by dataStore(
    fileName = "reboost_lateralMenu.json",
    serializer = DataStoreJsonSerializer(
        serializer = LateralMenuSettings.serializer(),
        defaultValue = LateralMenuSettingsDefault.default
    )
)

@Composable
internal fun rememberLateralMenuSettings(): LateralMenuSettings? {
    val context = LocalContext.current
    val settingsStore = remember(context) { context.lateralMenuSettingsDataStore }
    val settings by settingsStore.data.collectAsState(
        initial = null,
    )
    return settings
}

