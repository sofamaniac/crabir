package com.sofamaniac.crabir.settings

import android.util.Log
import androidx.datastore.core.Serializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class DataStoreJsonSerializer<T>(
    private val serializer: KSerializer<T>,
    override val defaultValue: T,
) :
    Serializer<T> {

    override suspend fun readFrom(input: InputStream): T {
        return try {
            Json.decodeFromString(serializer, input.readBytes().decodeToString())

        } catch (e: SerializationException) {
            Log.e("DataStoreJsonSerializer", "readFrom: $e")
            defaultValue
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        output.write(Json.encodeToString(serializer, t).encodeToByteArray())
    }
}
