package com.sofamaniac.crabir.settings.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.sofamaniac.crabir.domain.model.Quality
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import kotlinx.serialization.Serializable

@Serializable
enum class NetworkPolicy {
    /** Always load image, regardless of the network state. */
    Always,

    /** Never load image. */
    Never,

    /** Load image only when the device is connected to a wifi network. */
    OnWifi,
}

@Serializable
enum class VideoQuality {
    Low,
    High,
    Auto,
}

@Serializable
data class ImageQualitySettings(
    val onWifi: Quality,
    val onMobile: Quality,
    val loadImage: NetworkPolicy,
)

@Serializable
data class VideoQualitySettings(
    val onWifi: VideoQuality,
    val onMobile: VideoQuality,
    val autostart: NetworkPolicy,
)


@Serializable
data class DataSettings(
    val imageQuality: ImageQualitySettings,
    val videoQuality: VideoQualitySettings,
)

object DataSettingsDefault {
    val defaultImageQuality = ImageQualitySettings(
        onWifi = Quality.High,
        onMobile = Quality.Medium,
        loadImage = NetworkPolicy.Always,
    )
    val defaultVideoQuality = VideoQualitySettings(
        onWifi = VideoQuality.High,
        onMobile = VideoQuality.Low,
        autostart = NetworkPolicy.Always,
    )

    val defaultDataSettings = DataSettings(
        imageQuality = defaultImageQuality,
        videoQuality = defaultVideoQuality,
    )
}

val Context.dataSettingsStore by dataStore(
    fileName = "crabir_dataSettings.json",
    serializer = DataStoreJsonSerializer(
        DataSettings.serializer(),
        DataSettingsDefault.defaultDataSettings
    )
)

@Composable
fun rememberDataSettings(): DataSettings {
    val context = LocalContext.current
    val dataSettingsStore = remember(context) { context.dataSettingsStore }
    val dataSettings by dataSettingsStore.data.collectAsState(
        initial = DataSettingsDefault.defaultDataSettings,
    )
    return dataSettings
}

@Serializable
object DataSettingsRoute : Route
