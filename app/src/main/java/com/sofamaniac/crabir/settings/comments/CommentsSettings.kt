package com.sofamaniac.crabir.settings.comments

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.DataStoreJsonSerializer
import com.sofamaniac.crabir.settings.views.ImageHeight
import kotlinx.serialization.Serializable

object CommentsSettingsDefault {
    val default = CommentsSettings(
        useRecommendedSort = true,
        preferredSort = Sort.Best,
        showNavigationBar = true,
        useVolumeKeyNavigation = false,
        postMediaPreview = ImageHeight.Full,
        buttonsAlwaysVisible = false,
        hideButtonsAfterVote = false,
        collapseAutoMod = false,
        collapseDisruptive = false,
        showPostUpvotePercentage = false,
        threadLevelIndicator = ThreadIndicator.Line,
    )
}

@Serializable
data class CommentsSettings(
    val useRecommendedSort: Boolean,
    val preferredSort: Sort,
    val showNavigationBar: Boolean,
    val useVolumeKeyNavigation: Boolean,
    val postMediaPreview: ImageHeight,
    val buttonsAlwaysVisible: Boolean,
    val hideButtonsAfterVote: Boolean,
    val collapseAutoMod: Boolean,
    val collapseDisruptive: Boolean,
    val showPostUpvotePercentage: Boolean,
    val threadLevelIndicator: ThreadIndicator,
)

@Serializable
enum class ThreadIndicator {
    Line,
    LineColor,
}

val Context.commentsSettingsDataStore by dataStore(
    fileName = "reboost_comments_settings.json",
    serializer = DataStoreJsonSerializer(
        serializer = CommentsSettings.serializer(),
        defaultValue = CommentsSettingsDefault.default
    )
)

@Composable
internal fun rememberCommentsSettings(): CommentsSettings? {
    val context = LocalContext.current
    val commentsSettingsStore = remember(context) { context.commentsSettingsDataStore }
    val commentsSettings by commentsSettingsStore.data.collectAsState(
        initial = null,
    )
    return commentsSettings
}


@Serializable
object CommentsSettingsRoute : Route
