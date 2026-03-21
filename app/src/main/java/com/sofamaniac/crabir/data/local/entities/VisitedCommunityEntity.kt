package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.data.remote.dto.Timeframe
import com.sofamaniac.crabir.data.remote.dto.post.Sort
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.settings.views.Views
import kotlinx.serialization.json.Json

@Entity(tableName = "visitedCommunity")
data class VisitedCommunityEntity(
    @PrimaryKey
    val id: String = "",
    val sort: Sort? = null,
    val timeframe: Timeframe? = null,
    val view: Views? = null,
    val columns: Int? = null,
    val data: String? = null,
) {
    fun getData(): SubredditData? {
        return data?.let { Json.decodeFromString(it) }
    }

    fun copy(
        id: String = this.id,
        sort: Sort? = this.sort,
        timeframe: Timeframe? = this.timeframe,
        view: Views? = this.view,
        columns: Int? = this.columns,
        data: SubredditData? = this.getData()
    ): VisitedCommunityEntity {
        val newData = data?.let { Json.encodeToString(it) }
        return copy(
            id = id,
            sort = sort,
            timeframe = timeframe,
            view = view,
            columns = columns,
            data = newData
        )
    }
}
