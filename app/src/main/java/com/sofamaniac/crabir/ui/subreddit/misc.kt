package com.sofamaniac.crabir.ui.subreddit

import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity

@Composable
fun defaultCommunityEntity(name: String, displayName: String): CommunityViewEntity {
    val defaultView = LocalViewSettings.current.defaultView
    val defaultColumns = LocalViewSettings.current.defaultColumns
    return CommunityViewEntity(name, displayName, view = defaultView, columns = defaultColumns)
}