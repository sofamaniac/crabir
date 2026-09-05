package com.sofamaniac.crabir.data.local.entities

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecodingException

@Entity(tableName = "votableTable")
data class VotableEntity(
    @PrimaryKey
    val id: Fullname,
    val data: String,
)

@OptIn(ExperimentalSerializationApi::class)
fun VotableEntity.asVotableData(): VotableData {
    return Json.safeDecodeFromString<PostData>(data)
        ?: Json.safeDecodeFromString<CommentData>(data)
        ?: Json.safeDecodeFromString<CommentType.Comment>(data)
        ?: Json.safeDecodeFromString<CommentType.More>(data)
        ?: throw SerializationException("VotableEntity: Missing decode from type $this")
}


@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> Json.safeDecodeFromString(s: String): T? {
    return try {
        Json.decodeFromString<T>(s)
    } catch (e: JsonDecodingException) {
        Log.e("safeDecodeFromString", e.stackTraceToString())
        null
    }
}
