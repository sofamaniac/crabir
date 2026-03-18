package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import kotlinx.serialization.json.Json

@Entity(tableName = "visitedPosts")
data class VisitedPostEntity(
    @PrimaryKey
    val id: Fullname,
    val post: String,
    val visitedAt: Long,
)

fun VisitedPostEntity.toDomainModel(): PostData {
    return Json.decodeFromString(post)
}

fun PostData.toEntity(timestamp: Long = System.currentTimeMillis()): VisitedPostEntity {
    return VisitedPostEntity(
        id = name,
        visitedAt = timestamp,
        post = Json.encodeToString(this),
    )
}
