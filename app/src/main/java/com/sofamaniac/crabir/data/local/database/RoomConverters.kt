package com.sofamaniac.crabir.data.local.database

import androidx.room.TypeConverter
import com.sofamaniac.crabir.data.remote.dto.SubredditInfo
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MessageType
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import kotlinx.serialization.json.Json
import kotlin.time.Instant

class RoomConverters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.let { Json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromFullname(fullname: Fullname): String {
        return fullname.name
    }

    @TypeConverter
    fun toFullname(name: String): Fullname {
        return Fullname(name)
    }

    @TypeConverter
    fun toInfoList(value: String): List<SubredditInfo> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromInfoList(value: List<SubredditInfo>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromParsedMarkdown(value: ParsedMarkdown): String {
        return value.markdown
    }

    @TypeConverter
    fun toParsedMarkdown(value: String): ParsedMarkdown {
        return ParsedMarkdown(value)
    }

    @TypeConverter
    fun fromInstant(value: kotlin.time.Instant): Long {
        return value.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long): kotlin.time.Instant {
        return Instant.fromEpochMilliseconds(value)
    }

    @TypeConverter
    fun fromMessageType(value: MessageType): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toMessageType(value: String): MessageType {
        return Json.decodeFromString(value)
    }
}
