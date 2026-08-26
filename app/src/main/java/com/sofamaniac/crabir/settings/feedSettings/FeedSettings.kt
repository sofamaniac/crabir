package com.sofamaniac.crabir.settings.feedSettings

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import kotlinx.serialization.Serializable

@Serializable
data class FeedSettings(
    val homeSort: Sort = Sort.Hot,
    val homeTimeframe: Timeframe? = null,
    val defaultSort: Sort = Sort.Hot,
    val defaultTimeframe: Timeframe? = null,
    val rememberSort: Boolean = true,
)

@Serializable
object FeedSettingsRoute : Route

@Serializable
object SortManagerRoute : Route

val Context.feedSettingsStore by dataStore(
    fileName = "reboost_feedSettings.json",
    serializer = DataStoreJsonSerializer(
        serializer = FeedSettings.serializer(),
        defaultValue = FeedSettings()
    )
)

@Composable
internal fun rememberFeedSettings(): FeedSettings? {
    val context = LocalContext.current
    val settingsStore = remember(context) { context.feedSettingsStore }
    val settings by settingsStore.data.collectAsState(
        initial = null,
    )
    return settings
}

