package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.settings.views.Views

@Entity(tableName = "visitedCommunity")
class VisitedCommunityEntity(
    @PrimaryKey
    var id: String = "",
    var sort: Sort?,
    var timeframe: Timeframe?,
    var View: Views?
)
