package com.sofamaniac.crabir.settings.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.sofamaniac.crabir.R
import kotlinx.serialization.Serializable

enum class Views {
    Card,
    Compact;
    //    SmallCard,
    //    Dense,
    //    Image,
    //    Swipe;

    fun toStringResource(): Int {
        return when (this) {
            Card -> R.string.ViewCard
            Compact -> R.string.ViewCompact
            //            SmallCard -> R.string.ViewSmallCard
            //            Dense -> R.string.ViewDense
            //            Image -> R.string.ViewImage
            //            Swipe -> R.string.ViewSwipe
        }
    }
}

@Serializable
data class ViewSettings(
    val defaultView: Views = Views.Card,
    val defaultColumns: Int = 1,
    val rememberView: Boolean = true,
    val rememberColumns: Boolean = true,
    //    val postFontSettings: FontSettings = FontSettings(),
    //    val commentFontSettings: FontSettings = FontSettings(),
    val prefixCommunity: Boolean = true,
    val cardSettings: CardSettings = CardSettings(),
    val rememberedViews: Map<String, Views> = emptyMap(),
    val rememberedColumns: Map<String, Int> = emptyMap(),
)

@Serializable
data class CardSettings(
    val showSubredditIcon: Boolean = true,
    val enableFullHeightImage: Boolean = true,
    val enableTextPreview: Boolean = true,
    val thumbnailForLinkPreview: Boolean = true,
    val maxLines: Int = 5,
    val roundedCorners: Boolean = false,
)

@Serializable
data class FontSettings(
    val fontSize: Float = 12f,
)

val Context.viewSettingDataStore by dataStore(
    fileName = "reboost_view.json",
    serializer = com.sofamaniac.crabir.settings.DataStoreJsonSerializer(
        serializer = ViewSettings.serializer(),
        defaultValue = ViewSettings()
    )
)

@Composable
internal fun rememberViewSettings(): ViewSettings? {
    val context = LocalContext.current
    val viewSettingDataStore = remember(context) { context.viewSettingDataStore }
    val viewSettings by viewSettingDataStore.data.collectAsState(
        initial = null,
    )
    return viewSettings
}
