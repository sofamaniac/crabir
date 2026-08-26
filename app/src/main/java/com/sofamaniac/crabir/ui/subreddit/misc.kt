package com.sofamaniac.crabir.ui.subreddit

import android.util.Log
import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.LocalFeedSettings
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort

@Composable
private fun defaultCommunityEntity(name: String, displayName: String): CommunityViewEntity {
    Log.d("defaultCommunityEntity", "name: $name, displayName: $displayName")
    val viewSettings = LocalViewSettings.current
    val feedSettings = LocalFeedSettings.current
    Log.d("defaultCommunityEntity", "default timeframe: ${feedSettings.defaultTimeframe}")
    return CommunityViewEntity(
        name, displayName,
        sort = feedSettings.defaultSort,
        timeframe = feedSettings.defaultTimeframe,
        view = viewSettings.defaultView,
        columns = viewSettings.defaultColumns
    )
}

@Composable
fun getCommunityViewEntity(
    name: String,
    displayName: String,
    defaultSort: Sort = LocalFeedSettings.current.defaultSort,
    defaultTimeframe: Timeframe? = LocalFeedSettings.current.defaultTimeframe,
): CommunityViewEntity {
    val entity = LocalViewSettings.current.rememberedViews[name]
    val feedSettings = LocalFeedSettings.current
    val viewSettings = LocalViewSettings.current
    if (entity == null) {
        return defaultCommunityEntity(name, displayName)
    } else {
        return entity.let {
            if (!feedSettings.rememberSort) {
                it.copy(sort = defaultSort, timeframe = defaultTimeframe)
            } else {
                val sort = it.sort ?: defaultSort
                val timeframe = it.timeframe ?: defaultTimeframe
                it.copy(sort = sort, timeframe = timeframe)
            }
        }.let {
            if (!viewSettings.rememberView) {
                it.copy(view = viewSettings.defaultView, columns = viewSettings.defaultColumns)
            } else {
                val view = it.view ?: viewSettings.defaultView
                val columns = it.columns ?: viewSettings.defaultColumns
                it.copy(view = view, columns = columns)
            }
        }
    }
}