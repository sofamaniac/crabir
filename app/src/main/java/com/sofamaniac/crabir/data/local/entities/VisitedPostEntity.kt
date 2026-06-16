package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.domain.model.Fullname

@Entity(tableName = "visitedPosts")
data class VisitedPostEntity(
    @PrimaryKey
    val id: Fullname,
    val visitedAt: Long,
    val visitedBy: Int,
)
