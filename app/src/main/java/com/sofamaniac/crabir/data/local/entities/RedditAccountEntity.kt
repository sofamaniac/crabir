package com.sofamaniac.crabir.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sofamaniac.crabir.domain.model.AuthStateSerializer
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.serialization.json.Json

@Entity(tableName = "accounts", indices = [Index(value = ["name"], unique = true)])
data class RedditAccountEntity(
    @PrimaryKey()
    val id: Int = 0,
    val name: String,
    val info: String,
    val authState: String,
    val isActive: Boolean = false,
)


fun RedditAccountEntity.toDomainModel(): RedditAccount {
    return RedditAccount(
        id = id,
        info = Json.decodeFromString(info),
        auth = Json.decodeFromString(AuthStateSerializer, authState),
    )
}

fun RedditAccount.toEntity(): RedditAccountEntity {
    return RedditAccountEntity(
        info = Json.encodeToString(info),
        authState = Json.encodeToString(AuthStateSerializer, auth),
        name = info?.name?.name ?: "Anonymous",
    ).let {
        if (!isUninitialized()) {
            it.copy(id = id)
        } else {
            it
        }
    }
}