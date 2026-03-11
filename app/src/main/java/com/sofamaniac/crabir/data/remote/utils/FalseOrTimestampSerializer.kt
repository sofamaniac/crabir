/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.longOrNull
import kotlin.time.Instant

object FalseOrTimestampSerializer : KSerializer<Instant?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FalseOrTimestamp", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> {
                if (element.isString && element.content == "false") null
                else if (element.booleanOrNull != null) null
                else Instant.fromEpochSeconds(element.longOrNull ?: 0L)
            }

            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: Instant?) {
        encoder.encodeLong(value?.epochSeconds ?: 0L)
    }
}
