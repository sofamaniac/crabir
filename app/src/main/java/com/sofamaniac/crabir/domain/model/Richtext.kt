package com.sofamaniac.crabir.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.sofamaniac.crabir.ui.richtext.RichtextStyle
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

    interface ContainerBlock
    interface LeafBlock
    interface TextNode : LeafBlock

    @Serializable
    @SerialName("text")
    data class Text(
        @SerialName("t") val text: String,
        @SerialName("f") val modifiers: List<TextModifier> = emptyList(),
    ) : Richtext(), TextNode

    @Serializable
    @SerialName("h")
    data class Heading(
        @SerialName("l") val level: Int,
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("hr")
    class HorizontalRule() : Richtext(), LeafBlock

    @Serializable
    @SerialName("par")
    data class Paragraph(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("li")
    data class ListItem(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("list")
    data class ListBlock(
        @SerialName("c") val children: List<Richtext>,
        @SerialName("o") val ordered: Boolean,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("blockquote")
    data class Blockquote(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("code")
    data class Code(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("raw")
    data class Raw(
        @SerialName("t") val text: String,
    ) : Richtext(), TextNode

    @Serializable
    @SerialName("table")
    data class Table(
        @SerialName("h") val headers: List<TableHeader>,
        @SerialName("c") val rows: List<List<TableCell>>,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("spoilertext")
    data class Spoiler(
        @SerialName("c") val children: List<Richtext>,
    ) : Richtext(), ContainerBlock

    @Serializable
    @SerialName("link")
    data class Link(
        @SerialName("t") val text: String,
        @SerialName("u") val url: String,
        @SerialName("f") val modifiers: List<TextModifier> = emptyList(),
    ) : Richtext(), TextNode

    @Serializable
    @SerialName("r/")
    data class CommunityLink(
        @SerialName("t") val community: String,
        @SerialName("l") val l: Boolean,
    ) : Richtext(), TextNode

    @Serializable
    @SerialName("u/")
    data class UserLink(
        @SerialName("t") val user: String,
        @SerialName("l") val l: Boolean,
    ) : Richtext(), TextNode

    @Serializable
    @SerialName("img")
    data class Image(val id: String) : Richtext(), LeafBlock

    @Serializable
    @SerialName("gif")
    data class Gif(val id: String) : Richtext(), LeafBlock

    @Serializable
    @SerialName("video")
    data class Video(val id: String) : Richtext(), LeafBlock

    @Serializable
    @SerialName("br")
    data object LineBreak : Richtext(), LeafBlock
}

@Serializable
data class TableHeader(
    @SerialName("a") val alignment: TableAlignment,
    @SerialName("c") val children: List<Richtext>,
)

@Serializable
data class TableCell(
    @SerialName("c") val children: List<Richtext>,
)

@Serializable(with = TableAlignmentSerializer::class)
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
data class TextModifier(val style: Int, val start: Int, val length: Int) {
    val isBold: Boolean = style and TextStyle.Bold.value != 0
    val isItalic: Boolean = style and TextStyle.Italic.value != 0
    val isUnderline: Boolean = style and TextStyle.Underline.value != 0
    val isStrikethrough: Boolean = style and TextStyle.Strikethrough.value != 0
    val isSuperscript: Boolean = style and TextStyle.Superscript.value != 0
    val isInlineCode: Boolean = style and TextStyle.InlineCode.value != 0

    fun toSpanStyle(style: RichtextStyle): SpanStyle {
        val fontStyle = if (isItalic) FontStyle.Italic else null
        val fontWeight = if (isBold) FontWeight.Bold else null
        val fontFamily = if (isInlineCode) FontFamily.Monospace else null
        val decorations = mutableListOf<TextDecoration>()
        if (isUnderline) decorations.add(TextDecoration.Underline)
        if (isStrikethrough) decorations.add(TextDecoration.LineThrough)
        val background = if (isInlineCode) style.codeBackground else Color.Unspecified
        val fontSize = if (isSuperscript) 12.sp else TextUnit.Unspecified
        val baselineShift = if (isSuperscript) BaselineShift.Superscript else null
        return SpanStyle(
            background = background,
            baselineShift = baselineShift,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            fontFamily = fontFamily,
            textDecoration = if (decorations.isNotEmpty()) TextDecoration.combine(decorations) else null
        )
    }
}

object TextModifierSerializer : KSerializer<TextModifier> {
    private val delegate = ListSerializer(Int.serializer())
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: TextModifier) {
        encoder.encodeSerializableValue(delegate, listOf(value.style, value.start, value.length))
    }

    override fun deserialize(decoder: Decoder): TextModifier {
        val list = decoder.decodeSerializableValue(delegate)
        return TextModifier(list[0], list[1], list[2])
    }
}

object TableAlignmentSerializer : KSerializer<TableAlignment> {
    private val delegate = String.serializer()
    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: TableAlignment) {
        val s = when (value) {
            TableAlignment.Right -> "R"
            TableAlignment.Left -> "L"
            TableAlignment.Center -> "C"
        }
        encoder.encodeSerializableValue(delegate, s)
    }

    override fun deserialize(decoder: Decoder): TableAlignment {
        val s = decoder.decodeSerializableValue(delegate)
        return when (s) {
            "R" -> TableAlignment.Right
            "L" -> TableAlignment.Left
            "C" -> TableAlignment.Center
            else -> TableAlignment.Center
        }
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
