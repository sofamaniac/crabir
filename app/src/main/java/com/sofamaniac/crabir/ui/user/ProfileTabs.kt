package com.sofamaniac.crabir.ui.user

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.R
import kotlinx.serialization.Serializable

@Serializable
@Keep
enum class ProfileTabs {
    Overview, About, Posts, Comments, Saved, Upvoted, Downvoted, Hidden;

    @Composable
    fun stringResource(): String {
        return androidx.compose.ui.res.stringResource(
            when (this) {
                Overview -> R.string.profile_tab_overview
                About -> R.string.profile_tab_about
                Posts -> R.string.profile_tab_submitted
                Comments -> R.string.profile_tab_comments
                Saved -> R.string.profile_tab_saved
                Upvoted -> R.string.profile_tab_upvoted
                Downvoted -> R.string.profile_tab_downvoted
                Hidden -> R.string.profile_tab_hidden
            }
        )
    }

    companion object {
        val publicTabs = listOf(Overview, About, Posts, Comments)
        fun fromString(string: String): ProfileTabs {
            return when (string.lowercase()) {
                "about" -> About
                "submitted" -> Posts
                "comments" -> Comments
                "saved" -> Saved
                "upvoted" -> Upvoted
                "downvoted" -> Downvoted
                "hidden" -> Hidden
                else -> Overview
            }
        }
    }
}
