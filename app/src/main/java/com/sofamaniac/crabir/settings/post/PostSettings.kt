package com.sofamaniac.crabir.settings.post

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Singleton

@Serializable
data class AwardSettings(
    val showAwards: Boolean,
    val clickableAwards: Boolean,
)

@Serializable
data class FlairSettings(
    val showFlair: Boolean,
    val showFlairColor: Boolean,
    val showFlairEmoji: Boolean,
    val clickable: Boolean,
)

@Serializable
data class InfoSettings(
    val showAuthor: Boolean,
    val clickableAuthor: Boolean,
    val clickableCommunity: Boolean,
)

@Serializable
data class ButtonsSettings(
    val hide: Boolean,
    val markAsRead: Boolean,
    val share: Boolean,
    val comments: Boolean,
    val openInApp: Boolean,
)

@Serializable
data class LinksSettings(
    val upvoteOnSave: Boolean,
    val startMuted: Boolean,
)

@Serializable
data class PostSettings(
    //val awardSettings: AwardSettings,
    val flairSettings: FlairSettings,
    val infoSettings: InfoSettings,
    val buttonsSettings: ButtonsSettings,
    val linksSettings: LinksSettings,
)

object PostSettingsDefaults {
    val defaultAwardSettings = AwardSettings(showAwards = false, clickableAwards = false)
    val defaultFlairSettings = FlairSettings(
        showFlair = true,
        showFlairColor = true,
        showFlairEmoji = true,
        clickable = true
    )
    val defaultInfoSettings =
        InfoSettings(showAuthor = true, clickableAuthor = true, clickableCommunity = true)
    val defaultButtonsSettings = ButtonsSettings(
        hide = false,
        markAsRead = false,
        share = false,
        comments = true,
        openInApp = true
    )

    val defaultLinksSettings = LinksSettings(
        upvoteOnSave = false,
        startMuted = false
    )

    val defaultPostSettings = PostSettings(
        //awardSettings = defaultAwardSettings,
        flairSettings = defaultFlairSettings,
        infoSettings = defaultInfoSettings,
        buttonsSettings = defaultButtonsSettings,
        linksSettings = defaultLinksSettings
    )

}

@Serializable
object PostSettingsRoute : Route

val Context.postSettingsDataStore by dataStore(
    fileName = "reboost_postSettings.json",
    serializer = DataStoreJsonSerializer(
        serializer = PostSettings.serializer(),
        defaultValue = PostSettingsDefaults.defaultPostSettings
    )
)

@Composable
internal fun rememberPostsSettings(): PostSettings? {
    val context = LocalContext.current
    val postSettingsDataStore = remember(context) { context.postSettingsDataStore }
    val postSettings by postSettingsDataStore.data.collectAsState(
        initial = null,
    )
    return postSettings
}


interface PostSettingsRepository {
    val postSettings: Flow<PostSettings>
}

@Singleton(binds = [PostSettingsRepository::class])
class PostSettingsRepositoryImpl(
    private val context: Context,
) : PostSettingsRepository {
    override val postSettings: Flow<PostSettings> = context.postSettingsDataStore.data
}