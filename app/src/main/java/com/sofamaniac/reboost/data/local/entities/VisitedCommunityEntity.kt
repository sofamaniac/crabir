package com.sofamaniac.reboost.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.reboost.data.remote.dto.Timeframe
import com.sofamaniac.reboost.data.remote.dto.post.Sort

@Entity(tableName = "visitedCommunity")
class VisitedCommunityEntity(
    @PrimaryKey
    var id: String = "",
    var sort: Sort,
    var timeframe: Timeframe?
)
