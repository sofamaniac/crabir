package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import kotlinx.serialization.json.Json

@Entity(tableName = "votableTable")
data class VotableEntity(
    @PrimaryKey
    val id: Fullname,
    val data: String
)

fun PostData.toEntity(): VotableEntity = VotableEntity(
    id = name,
    data = Json.encodeToString(this)
)

fun CommentData.toEntity(): VotableEntity = VotableEntity(
    id = name,
    data = Json.encodeToString(this)
)

fun VotableEntity.asVotableData() = when (id.name.startsWith("t3_")) {
    true -> Json.decodeFromString<PostData>(data)
    else -> Json.decodeFromString<CommentData>(data)
}


fun VotableEntity.asPost(): PostData? = Json.decodeFromString(data) as? PostData
fun VotableEntity.asComment(): CommentData? = Json.decodeFromString(data) as? CommentData