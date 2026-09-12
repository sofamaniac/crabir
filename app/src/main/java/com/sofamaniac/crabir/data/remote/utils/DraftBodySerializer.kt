package com.sofamaniac.crabir.data.remote.utils

import android.util.Log
import com.sofamaniac.crabir.data.remote.dto.DraftBody
import com.sofamaniac.crabir.domain.model.RichtextDocument
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull


object DraftBodySerializer : KSerializer<DraftBody?> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DraftBody", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DraftBody? {
        val jsonDecoder = decoder as? JsonDecoder
            ?: throw SerializationException("Expected JsonDecoder")
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> {
                if (element.isString) DraftBody.Text(element.content)
                else if (element.contentOrNull == null) null
                else {
                    Log.e("DraftBodySerializer", "Unknown element type: $element")
                    null
                }
            }

            is JsonObject -> {
                val richtext =
                    jsonDecoder.json.decodeFromJsonElement(RichtextDocument.serializer(), element)
                DraftBody.Richtext(richtext)
            }

            else -> {
                Log.e("EmptyStringOrListingSerializer", "Unknown element type: $element")
                null
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: DraftBody?) {
        if (value == null) {
            encoder.encodeNull()
            return
        } else {
            DraftBody.serializer().serialize(encoder, value)
        }
    }
}
