package com.sofamaniac.crabir.ui.richtext

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.CrabirUriHandler
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.Richtext
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.model.TableAlignment
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SimpleImageRoute
import com.sofamaniac.crabir.ui.protectedTouch

@Composable
fun Richtext(
    document: RichtextDocument,
    modifier: Modifier = Modifier,
    mediaMetadata: Map<String, MediaMetadata>,
    style: RichtextStyle = defaultRichtextStyle(),
) {
    val configuration = DefaultRichtextComponents()
    val context = Context(
        inCode = false,
        inSpoiler = false,
        inList = ListState.None,
        configuration = configuration,
        mediaMetadata = mediaMetadata,
        style = style
    )
    CompositionLocalProvider(
        LocalUriHandler provides CrabirUriHandler(
            LocalNavController.current,
            LocalUriHandler.current
        )
    ) {
        Column(
            modifier.semantics(mergeDescendants = true, properties = {}),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (child in document.document) {
                context.configuration.render(child, context)
            }
        }
    }
}

@Composable
fun Spoiler(spoiler: Richtext.Spoiler, context: Context) {
    var clicked by remember { mutableStateOf(context.inSpoiler) }
    val context = context.copy(inSpoiler = true)
    Column(
        modifier = Modifier
            .protectedTouch(enabled = !clicked) { clicked = true }
            .drawWithContent {
                drawContent()
                if (!clicked) {
                    drawRect(context.style.spoilerCurtain)
                }
            }) {
        for (child in spoiler.children) {
            context.configuration.render(child, context)
        }
    }
}

@Composable
internal fun InnerImage(media: MediaMetadata, caption: String? = null) {
    val url = when (media) {
        is MediaMetadata.Gif -> media.source?.gifUrl

        is MediaMetadata.Image -> media.source?.url

        MediaMetadata.Invalid -> null
    }
    if (url != null) {
        val navController = LocalNavController.current
        AsyncImage(
            model = url,
            contentDescription = caption,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController?.navigate(SimpleImageRoute(url))
                }
        )
    }
}


@Composable
fun Image(image: Richtext.Image, context: Context) {
    val media = context.mediaMetadata[image.id]
    if (media != null) {
        InnerImage(media, image.caption)
    }
}

@Composable
fun Video(video: Richtext.Video, context: Context) {
    val media = context.mediaMetadata[video.id]
    val navController = LocalNavController.current
    val uriHandler = LocalUriHandler.current
    Box(modifier = Modifier.clickable {
        uriHandler.openUri("https://v.redd.it/${video.id}")
    }) {
        AsyncImage("https://preview.redd.it/${video.id}.jpg", contentDescription = video.caption)
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = "Play video",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun Gif(image: Richtext.Gif, context: Context) {
    val media = context.mediaMetadata[image.id]
    if (image.id.contains("|")) {
        val id = image.id.split("|")[1]
        val gifUrl = "https://media.giphy.com/media/${id}/giphy.gif"
        val url = "https://giphy.com/gifs/${id}"
        val navController = LocalNavController.current
        AsyncImage(
            model = gifUrl,
            contentDescription = image.caption,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    navController?.navigate(SimpleImageRoute(gifUrl))
                }
        )
    } else if (media != null) {
        InnerImage(media, caption = image.caption)
    }
}

@Composable
fun ListBlock(list: Richtext.ListBlock, context: Context) {
    Column {
        for (child in list.children) {
            if (child !is Richtext.ListItem) continue
            ListItem(
                child,
                context.copy(inList = if (list.ordered) ListState.Ordered else ListState.Unordered),
                list.children.indexOf(child)
            )
        }
    }
}

@Composable
fun ListItem(item: Richtext.ListItem, context: Context, index: Int) {
    if (context.inList == ListState.Ordered) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${index + 1}.")
            Column {
                for (child in item.children) {
                    context.configuration.render(child, context)
                }
            }
        }
    } else {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("-")
            Column {
                for (child in item.children) {
                    context.configuration.render(child, context)
                }
            }
        }
    }
}

@Composable
fun Heading(heading: Richtext.Heading, context: Context) {
    val style = when (heading.level) {
        1 -> MaterialTheme.typography.headlineLarge
        2 -> MaterialTheme.typography.headlineMedium
        3 -> MaterialTheme.typography.headlineSmall
        4 -> MaterialTheme.typography.titleLarge
        5 -> MaterialTheme.typography.titleMedium
        6 -> MaterialTheme.typography.titleSmall
        else -> MaterialTheme.typography.headlineLarge
    }
    Column(modifier = Modifier.semantics(mergeDescendants = true, properties = { heading() })) {
        var index = 0
        while (index < heading.children.size) {
            if (heading.children[index] !is Richtext.TextNode) {
                context.configuration.render(heading.children[index], context)
                index++
                continue
            }
            val res = heading.children.buildAnnotatedString(index, context)
            val currentString = buildAnnotatedString {
                withStyle(style.toSpanStyle()) {
                    append(res.first)
                }
            }
            index = res.second
            Text(currentString)
        }
    }
}

@Composable
fun Blockquote(quote: Richtext.Blockquote, context: Context) {
    val style = context.style.blockquoteStyle
    Column(
        Modifier
            .drawBehind {
                drawLine(
                    color = style.color,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = style.strokeWidth.toPx()
                )
            }
            .padding(start = style.indent)
    ) {
        for (child in quote.children) {
            context.configuration.render(child, context)
        }
    }
}

@Composable
fun Code(code: Richtext.Code, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(context.style.codeBackground)
            .padding(all = 4.dp)
    ) {
        for (child in code.children) {
            context.configuration.render(child, context.copy(inCode = true))
        }
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
fun Table(table: Richtext.Table, context: Context) {
    val style = context.style.tableStyle
    fun TableAlignment.toAlignment(): Alignment = when (this) {
        TableAlignment.Right -> Alignment.CenterEnd
        TableAlignment.Left -> Alignment.CenterStart
        TableAlignment.Center -> Alignment.Center
    }

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Grid(config = {
            repeat(table.headers.size) {
                column(GridTrackSize.Auto)
            }
            repeat(table.rows.size + 1) {
                row(GridTrackSize.Auto)
            }
            gap(1.dp)
        }) {
            table.headers.forEachIndexed { index, header ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(style.headerBackground)
                        .padding(style.cellPadding)
                        .gridItem(row = 1, column = index + 1),
                    contentAlignment = header.alignment.toAlignment()
                ) {
                    Column {
                        for (child in header.children) {
                            context.configuration.render(child, context)
                        }
                    }
                }
            }
            table.rows.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, cell ->
                    val header = table.headers[columnIndex]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if ((rowIndex + columnIndex) % 2 == 0) style.rowBackground1 else style.rowBackground2
                            )
                            .padding(style.cellPadding)
                            .gridItem(row = rowIndex + 2, column = columnIndex + 1),
                        contentAlignment = header.alignment.toAlignment()
                    ) {
                        Column {
                            for (child in cell.children) {
                                context.configuration.render(child, context)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Paragraph(
    paragraph: Richtext.Paragraph,
    context: Context,
) {

    FlowRow(modifier = Modifier.height(IntrinsicSize.Min)) {
        var index = 0
        while (index < paragraph.children.size) {
            if (paragraph.children[index] !is Richtext.TextNode) {
                context.configuration.render(paragraph.children[index], context)
                index++
                continue
            }
            val res = paragraph.children.buildAnnotatedString(index, context)
            val currentString = res.first
            index = res.second
            Text(currentString)
        }
    }
}

fun Richtext.Text.toAnnotatedString(context: Context): AnnotatedString {
    var currentStart = 0
    return buildAnnotatedString {
        val outerStyle =
            if (context.inCode) SpanStyle(fontFamily = FontFamily.Monospace) else SpanStyle()
        withStyle(outerStyle) {
            for (modifier in modifiers) {
                if (currentStart < modifier.start) {
                    append(text.substring(currentStart, modifier.start))
                }
                withStyle(modifier.toSpanStyle(context.style)) {
                    append(text.substring(modifier.start, modifier.start + modifier.length))
                }
                currentStart = modifier.start + modifier.length
            }
            if (currentStart < text.length) {
                append(text.substring(currentStart, text.length))
            }
        }
    }
}

fun List<Richtext>.buildAnnotatedString(
    start: Int = 0,
    context: Context,
): Pair<AnnotatedString, Int> {
    var index = start
    var currentString = AnnotatedString("")
    while (index < size) {
        val ext = when (val child = this[index]) {
            is Richtext.Text -> {
                child.toAnnotatedString(context)
            }

            is Richtext.Link -> {
                child.toAnnotatedString(context)
            }

            is Richtext.CommunityLink -> {
                child.toAnnotatedString(context)
            }

            is Richtext.UserLink -> {
                child.toAnnotatedString(context)
            }

            is Richtext.Raw -> {
                AnnotatedString(child.text)
            }

            else -> {
                break
            }
        }
        currentString += ext
        index++
    }
    return Pair(currentString, index)
}

fun Richtext.Link.toAnnotatedString(context: Context): AnnotatedString {
    return buildAnnotatedString {
        val text = Richtext.Text(text, modifiers)
        withLink(LinkAnnotation.Url(url)) {
            withStyle(context.style.linkStyle)
            {
                append(text.toAnnotatedString(context))
            }
        }
    }
}

fun Richtext.CommunityLink.toAnnotatedString(context: Context): AnnotatedString {
    return buildAnnotatedString {
        withLink(LinkAnnotation.Url("https://www.reddit.com/r/$community")) {
            withStyle(context.style.linkStyle) {
                append("r/$community")
            }
        }
    }
}

fun Richtext.UserLink.toAnnotatedString(context: Context): AnnotatedString {
    return buildAnnotatedString {
        withLink(LinkAnnotation.Url("https://www.reddit.com/user/$user")) {
            withStyle(context.style.linkStyle) {
                append("u/$user")
            }
        }
    }
}
