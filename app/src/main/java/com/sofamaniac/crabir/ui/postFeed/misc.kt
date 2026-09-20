package com.sofamaniac.crabir.ui.postFeed

import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.LocalFeedSettings
import com.sofamaniac.crabir.LocalViewSettings
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.settings.views.Views

@Composable
fun defaultCommunityEntity(name: String, displayName: String): CommunityViewEntity {
    return CommunityViewEntity(
        name, displayName,
    )
}

data class SortFull(val sort: Sort, val timeframe: Timeframe?)

@Composable
fun getCommunitySort(
    name: String,
    defaultSort: Sort = LocalFeedSettings.current.defaultSort,
    defaultTimeframe: Timeframe? = LocalFeedSettings.current.defaultTimeframe,
): SortFull {
    val entity = LocalViewSettings.current.rememberedViews[name]
    val feedSettings = LocalFeedSettings.current
    if (entity == null || !feedSettings.rememberSort) {
        return SortFull(defaultSort, defaultTimeframe)
    }
    return SortFull(entity.sort ?: defaultSort, entity.timeframe ?: defaultTimeframe)
}

data class ViewFull(val view: Views, val columns: Int)

@Composable
fun getCommunityView(
    name: String,
    defaultView: Views = LocalViewSettings.current.defaultView,
    defaultColumns: Int = LocalViewSettings.current.defaultColumns,
): ViewFull {
    val entity = LocalViewSettings.current.rememberedViews[name]
    val viewSettings = LocalViewSettings.current
    if (entity == null || !viewSettings.rememberView) {
        return ViewFull(defaultView, defaultColumns)
    }
    return ViewFull(entity.view ?: defaultView, entity.columns ?: defaultColumns)
}
