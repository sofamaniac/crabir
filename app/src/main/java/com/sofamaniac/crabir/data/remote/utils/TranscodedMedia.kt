/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.utils

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.post.RedditVideo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/** If `transcoding_status` is different from `completed` returns null*/
object RedditVideoSerializer : KSerializer<RedditVideo?> {

    private val delegateSerializer = RedditVideo.serializer().nullable

    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: RedditVideo?) {
        require(encoder is JsonEncoder)
        if (value == null) {
            encoder.encodeNull()
        } else {
            val element = buildJsonObject {
                put("transcoding_status", JsonPrimitive("completed"))
                encoder.json.encodeToJsonElement(
                    RedditVideo.serializer(),
                    value
                ).jsonObject.forEach { (k, v) ->
                    put(k, v)
                }
            }
            encoder.encodeJsonElement(element)
        }
    }

    override fun deserialize(decoder: Decoder): RedditVideo? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonObject -> {
                val status = element.jsonObject["transcoding_status"]?.jsonPrimitive?.contentOrNull

                if (status == "completed") {
                    val filtered = JsonObject(element.filterKeys { it != "transcoding_status" })
                    jsonDecoder.json.decodeFromJsonElement(RedditVideo.serializer(), filtered)
                } else {
                    Log.i(
                        "RedditVideoSerializer",
                        "Transcoding status ${element.jsonObject["transcoding_status"]}"
                    )
                    null
                }
            }

            else -> {
                Log.e("RedditVideoDeserializer", "Expected JsonObject but got: $element")
                null
            }
        }

    }
}