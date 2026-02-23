@file:UseSerializers(ColorSerializer::class)

package com.sofamaniac.reboost.settings

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.dataStore
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ReboostTheme(
    val background: Color,
    val cardBackground: Color,
    val toolbarBackground: Color,
    val toolbarText: Color,
    val primaryColor: Color,
    val highlight: Color,
    val postTitle: Color,
    val readPost: Color,
    val announcement: Color,
    val contentColor: Color,
    val linkColor: Color,
    val secondaryText: Color,
    val downvote: Color,
)

val DefaultReboostTheme = ReboostTheme(
    background = Color.Black,
    cardBackground = Color.Black,
    toolbarBackground = Color.Black,
    toolbarText = Color.White,
    primaryColor = Color(0xffff6e40),
    secondaryText = Color(0xffb7b8bc),
    highlight = Color(0xffff0000),
    postTitle = Color(0xfff5f6f8),
    readPost = Color(0xffb7b8bc),
    announcement = Color(0xff00ff00),
    contentColor = Color(0xfff5f6f8),
    linkColor = Color(0xff4b91e2),
    downvote = Color(0xff9580ff),
)

val Context.themeDataStore by dataStore(
    fileName = "reboost_theme.json",
    serializer = DataStoreJsonSerializer(
        serializer = ReboostTheme.serializer(),
        defaultValue = DefaultReboostTheme
    )
)


object ColorSerializer : KSerializer<Color> {
    override val descriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeLong(value.value.toLong())
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeLong())
    }
}