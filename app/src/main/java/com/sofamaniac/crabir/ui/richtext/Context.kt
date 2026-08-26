package com.sofamaniac.crabir.ui.richtext

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.Richtext

enum class ListState {
    Unordered,
    Ordered,
    None,
}

data class Configuration(
    val components: RichtextComponents,
    val styles: RichtextStyle,
)

data class Context(
    val inSpoiler: Boolean,
    val inList: ListState,
    val style: SpanStyle?,
    val mediaMetadata: Map<String, MediaMetadata>,
    val configuration: Configuration,
    val threadId: String?,
)

@Immutable
data class RichtextStyle(
    val linkStyle: SpanStyle,
    val spoilerCurtain: Color,
    val codeBackground: Color,
    val blockquoteStyle: BlockquoteStyle,
    val tableStyle: TableStyle,
)

@Composable
fun defaultRichtextStyle(): RichtextStyle {
    val theme = LocalTheme.current
    val linkStyle = SpanStyle(color = theme.linkColor, textDecoration = TextDecoration.Underline)
    return RichtextStyle(
        linkStyle = linkStyle,
        spoilerCurtain = theme.contentColor,
        codeBackground = theme.secondaryText.copy(alpha = 0.3f),
        blockquoteStyle = defaultBlockquoteStyle(),
        tableStyle = defaultTableStyle()
    )
}

@Immutable
data class TableStyle(
    val headerBackground: Color,
    val rowBackground1: Color,
    val rowBackground2: Color,
    val cellPadding: Dp,
)

@Composable
fun defaultTableStyle(): TableStyle {
    val theme = LocalTheme.current
    val headerColor = theme.secondaryText.copy(alpha = 0.5f)
    val rowColor1 = theme.secondaryText.copy(alpha = 0.1f)
    val rowColor2 = theme.secondaryText.copy(alpha = 0.3f)
    return TableStyle(
        headerBackground = headerColor,
        rowBackground1 = rowColor1,
        rowBackground2 = rowColor2,
        cellPadding = 8.dp
    )
}

@Immutable
data class BlockquoteStyle(
    val color: Color,
    val strokeWidth: Dp,
    val indent: Dp,
)

@Composable
fun defaultBlockquoteStyle(): BlockquoteStyle {
    val theme = LocalTheme.current
    return BlockquoteStyle(
        color = theme.highlight,
        strokeWidth = 2.dp,
        indent = 8.dp,
    )
}

@Immutable
open class RichtextComponents(
    val paragraph: @Composable (Richtext.Paragraph, Context) -> Unit,
    val horizontalRule: @Composable () -> Unit,
    val blockquote: @Composable (Richtext.Blockquote, Context) -> Unit,
    val code: @Composable (Richtext.Code, Context) -> Unit,
    val table: @Composable (Richtext.Table, Context) -> Unit,
    val heading: @Composable (Richtext.Heading, Context) -> Unit,
    val listBlock: @Composable (Richtext.ListBlock, Context) -> Unit,
    val listItem: @Composable (Richtext.ListItem, Context) -> Unit,
    val spoiler: @Composable (Richtext.Spoiler, Context) -> Unit,
    val image: @Composable (Richtext.Image, Context) -> Unit,
    val gif: @Composable (Richtext.Gif, Context) -> Unit,
    val video: @Composable (Richtext.Video, Context) -> Unit,
) {
    @Composable
    fun render(e: Richtext, context: Context) {
        when (e) {
            is Richtext.Paragraph -> paragraph(e, context)
            is Richtext.HorizontalRule -> horizontalRule()
            is Richtext.Blockquote -> blockquote(e, context)
            is Richtext.Code -> code(e, context)
            is Richtext.Heading -> heading(e, context)
            is Richtext.ListBlock -> listBlock(e, context)
            is Richtext.ListItem -> listItem(e, context)
            is Richtext.Spoiler -> spoiler(e, context)
            is Richtext.Image -> image(e, context)
            is Richtext.Gif -> gif(e, context)
            is Richtext.Video -> video(e, context)
            is Richtext.Table -> {
                table(e, context)
            }

            is Richtext.Raw -> {
                val text = Richtext.Text(e.text, emptyList())
                Text(text.toAnnotatedString(context))
            }

            is Richtext.CommunityLink -> {
                Text(e.toAnnotatedString(context))
            }

            is Richtext.LineBreak -> {
                Spacer(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(1f)
                )
            }

            is Richtext.Link -> {
                Text(e.toAnnotatedString(context))
            }

            is Richtext.Text -> {
                Text(e.toAnnotatedString(context))
            }

            is Richtext.UserLink -> {
                Text(e.toAnnotatedString(context))
            }
        }
    }
}

class DefaultRichtextComponents : RichtextComponents(
    paragraph = { paragraph, ctx ->
        Paragraph(paragraph, ctx)
    },
    horizontalRule = {
        HorizontalDivider()
    },
    blockquote = { quote, ctx ->
        Blockquote(quote, ctx)
    },
    code = { code, ctx ->
        Code(code, ctx)
    },
    heading = { heading, ctx ->
        Heading(heading, ctx)
    },
    listBlock = { listBlock, ctx ->
        ListBlock(listBlock, ctx)
    },
    listItem = { listItem, ctx ->
    },
    spoiler = { spoiler, ctx ->
        Spoiler(spoiler, ctx)
    },
    image = { image, ctx ->
        Image(image, ctx)
    },
    gif = { gif, ctx ->
        Gif(gif, ctx)
    },
    table = { table, ctx ->
        Table(table, ctx)
    },
    video = { video, ctx ->
        Video(video, ctx)
    }
)

