package com.sofamaniac.crabir.domain.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("e")
sealed class Richtext() {


    @Serializable
    @SerialName("text")
    data class Text(
        @SerialName("t") val text: String,
        @SerialName("f") val modifiers: List<TextModifier> = emptyList(),
    ) :
        Richtext()

    @Serializable
    @SerialName("h")
    data class Heading(
        @SerialName("l") val level: Int,
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext()

    @Serializable
    @SerialName("hr")
    class HorizontalRule() : Richtext()

    @Serializable
    @SerialName("par")
    data class Paragraph(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext()

    @Serializable
    @SerialName("li")
    data class ListItem(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext()

    @Serializable
    @SerialName("list")
    data class ListBlock(
        @SerialName("c") val children: List<Richtext>,
        @SerialName("o") val ordered: Boolean,
    ) : Richtext()

    @Serializable
    @SerialName("blockquote")
    data class Blockquote(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext()

    @Serializable
    @SerialName("code")
    data class Code(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext()

    @Serializable
    @SerialName("raw")
    data class Raw(
        @SerialName("t") val text: String,
    ) : Richtext()

    @Serializable
    @SerialName("table")
    data class Table(
        @SerialName("h") val headers: List<TableHeader>,
        @SerialName("c") val rows: List<List<TableRow>>,
    ) : Richtext()

    @Serializable
    @SerialName("spoilertext")
    data class Spoiler(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext()

    @Serializable
    @SerialName("link")
    data class Link(
        @SerialName("t") val text: String,
        @SerialName("u") val url: String,
        @SerialName("f") val modifiers: List<TextModifier> = emptyList(),
    ) : Richtext()

    @Serializable
    @SerialName("r/")
    data class CommunityLink(
        @SerialName("t") val community: String,
        @SerialName("l") val l: Boolean,
    ) : Richtext()

    @Serializable
    @SerialName("u/")
    data class UserLink(@SerialName("t") val user: String, @SerialName("l") val l: Boolean) :
        Richtext()

    @Serializable
    @SerialName("img")
    data class Image(val id: String) : Richtext()

    @Serializable
    @SerialName("gif")
    data class Gif(val id: String) : Richtext()

    @Serializable
    @SerialName("br")
    data object LineBreak : Richtext()
}

@Serializable
data class TableHeader(
    @SerialName("a") val alignment: TableAlignment,
    @SerialName("c") val children: List<Richtext>,
)

@Serializable
data class TableRow(
    @SerialName("c") val children: List<Richtext>,
)

@Serializable
enum class TableAlignment {
    @SerialName("R")
    Right,

    @SerialName("L")
    Left,

    @SerialName("C")
    Center
}

@Serializable
data class RichtextDocument(val document: List<Richtext>)

@Serializable(with = TextModifierSerializer::class)
data class TextModifier(val style: Int, val start: Int, val end: Int)

object TextModifierSerializer : KSerializer<TextModifier> {
    private val delegate = ListSerializer(Int.serializer())
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: TextModifier) {
        encoder.encodeSerializableValue(delegate, listOf(value.style, value.start, value.end))
    }

    override fun deserialize(decoder: Decoder): TextModifier {
        val list = decoder.decodeSerializableValue(delegate)
        return TextModifier(list[0], list[1], list[2])
    }
}

enum class TextStyle(val value: Int) {
    Bold(1),
    Italic(2),
    Underline(4),
    Strikethrough(8),
    Superscript(32),
    InlineCode(64),
}
