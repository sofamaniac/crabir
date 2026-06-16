package com.sofamaniac.crabir.data.remote.utils

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive

object CommentSortSerializer : KSerializer<Sort?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CommentSort", PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(
        encoder: Encoder,
        value: Sort?
    ) {
        if (value != null) {
            Sort.serializer().serialize(encoder, value)
        } else {
            encoder.encodeNull()
        }
    }

    override fun deserialize(decoder: Decoder): Sort? {

        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive if element.isString -> when (element.content.lowercase()) {
                "top" -> Sort.Top
                "best" -> Sort.Best
                "new" -> Sort.New
                "controversial" -> Sort.Controversial
                "qa" -> Sort.Qa
                "old" -> Sort.Old
                "live" -> Sort.Live
                "random" -> Sort.Random
                else -> {
                    Log.e("CommentSortSerializer", "Unknown sort: $element")
                    Sort.Best
                }
            }

            else -> {
                Log.e("CommentSortSerializer", "Unexpected type $element")
                null
            }
        }
    }
}