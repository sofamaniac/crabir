package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.settings.views.Views
import kotlinx.serialization.Serializable

@Entity(tableName = "visitedCommunity")
@Serializable
data class CommunityViewEntity(
    @PrimaryKey
    val name: String,
    val displayName: String,
    val sort: Sort? = null,
    val timeframe: Timeframe? = null,
    val view: Views? = null,
    val columns: Int? = null,
)
