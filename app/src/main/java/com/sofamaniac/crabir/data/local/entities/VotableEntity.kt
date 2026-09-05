package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.serialization.json.Json

@Entity(tableName = "votableTable")
data class VotableEntity(
    @PrimaryKey
    val id: Fullname,
    val data: String,
)

fun VotableEntity.asVotableData(): VotableData {
    return Json.safeDecodeFromString<PostData>(data)
        ?: Json.safeDecodeFromString<CommentData>(data)
        ?: Json.safeDecodeFromString<CommentType.Comment>(data)
        ?: Json.safeDecodeFromString<CommentType.More>(data)
        ?: throw Exception("VotableEntity: Missing decode from type $this")
}


inline fun <reified T> Json.safeDecodeFromString(s: String): T? {
    return try {
        Json.decodeFromString<T>(s)
    } catch (e: Exception) {
        null
    }
}

fun VotableEntity.asPost(): PostData? = Json.decodeFromString(data) as? PostData
fun VotableEntity.asComment(): CommentData? = Json.decodeFromString(data) as? CommentData
