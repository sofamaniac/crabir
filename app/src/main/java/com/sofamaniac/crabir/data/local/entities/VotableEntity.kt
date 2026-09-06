package com.sofamaniac.crabir.data.local.entities

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.VotableData
import com.sofamaniac.crabir.toOption
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

@Entity(tableName = "votableTable")
data class VotableEntity(
    @PrimaryKey
    val id: Fullname,
    val data: String,
)

fun VotableEntity.asVotableData(): VotableData {
    return Json.safeDecodeFromString<PostData>(data)
        ?: Json.safeDecodeFromString<CommentType.Comment>(data)
        ?: Json.safeDecodeFromString<CommentType.More>(data)
        ?: throw SerializationException("VotableEntity: Missing decode from type $this")
}

inline fun <reified T> VotableEntity.into(): T? {
    return Json.safeDecodeFromString<T>(data)
}


inline fun <reified T> Json.safeDecodeFromString(s: String): T? {
    return runCatching {
        Json.decodeFromString<T>(s)
    }.onFailure { e ->
        Log.e("safeDecodeFromString", e.stackTraceToString())
    }
        .toOption()
}
