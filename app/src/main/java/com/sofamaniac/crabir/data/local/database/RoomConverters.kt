package com.sofamaniac.crabir.data.local.database

import androidx.room.TypeConverter
import com.sofamaniac.crabir.data.remote.dto.SubredditInfo
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.MessageType
import com.sofamaniac.crabir.domain.model.ParentInfo
import com.sofamaniac.crabir.domain.model.ParsedMarkdown
import kotlinx.serialization.json.Json
import kotlin.time.Instant

interface StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}

interface IntListConverter {
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.let { Json.decodeFromString(it) }
    }
}

interface FullnameConverter {

    @TypeConverter
    fun fromFullname(fullname: Fullname): String {
        return fullname.name
    }

    @TypeConverter
    fun toFullname(name: String): Fullname {
        return Fullname(name)
    }
}

interface InfoListConverter {
    @TypeConverter
    fun toInfoList(value: String): List<SubredditInfo> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromInfoList(value: List<SubredditInfo>): String {
        return Json.encodeToString(value)
    }
}

interface ParsedMarkdownConverter {
    @TypeConverter
    fun fromParsedMarkdown(value: ParsedMarkdown): String {
        return value.markdown
    }

    @TypeConverter
    fun toParsedMarkdown(value: String): ParsedMarkdown {
        return ParsedMarkdown(value)
    }
}

interface InstantConverter {
    @TypeConverter
    fun fromInstant(value: Instant): Long {
        return value.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }
}

interface MessageTypeConverter {
    @TypeConverter
    fun fromMessageType(value: MessageType): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toMessageType(value: String): MessageType {
        return Json.decodeFromString(value)
    }
}

interface ParentInfoConverter {
    @TypeConverter
    fun fromParentInfo(value: ParentInfo): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toParentInfo(value: String): ParentInfo {
        return Json.decodeFromString(value)
    }
}

interface ListFullnameConverter {
    @TypeConverter
    fun fromListFullname(value: List<Fullname>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toListFullname(value: String): List<Fullname> {
        return Json.decodeFromString(value)
    }
}

class RoomConverters : StringListConverter, IntListConverter, FullnameConverter, InfoListConverter,
    ParsedMarkdownConverter, InstantConverter, MessageTypeConverter, ParentInfoConverter,
    ListFullnameConverter
