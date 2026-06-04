package com.sofamaniac.crabir.data.local.database

import androidx.room.TypeConverter
import com.sofamaniac.crabir.data.remote.dto.SubredditInfo
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.serialization.json.Json

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
}
