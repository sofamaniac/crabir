/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.data.remote.utils

import android.util.Log
import com.sofamaniac.reboost.data.remote.dto.post.RedditVideo
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/** If `transcoding_status` is different from `completed` returns null*/
object RedditVideoSerializer : KSerializer<RedditVideo?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("RedditVideoSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: RedditVideo?) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): RedditVideo? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonObject -> {
                val status = element.jsonObject["transcoding_status"]?.jsonPrimitive?.contentOrNull

                if (status == "completed") {
                    jsonDecoder.json.decodeFromJsonElement(RedditVideo.serializer(), element)
                } else {
                    Log.i(
                        "TranscodedMedia",
                        "Transcoding status ${element.jsonObject["transcoding_status"]}"
                    )
                    null
                }
            }

            else -> {
                Log.e("TranscodedMedia", "Expected JsonObject but got: $element")
                null
            }
        }

    }
}