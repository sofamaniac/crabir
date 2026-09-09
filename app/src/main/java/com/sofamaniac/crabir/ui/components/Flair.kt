package com.sofamaniac.crabir.ui.components

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.crabir.data.remote.dto.LinkFlairRichtextEmoji
import com.sofamaniac.crabir.data.remote.dto.LinkFlairRichtextText
import com.sofamaniac.crabir.domain.model.Flair
import kotlin.math.max
import kotlin.math.min

@Composable
fun FlairRichtext(
    richText: List<LinkFlairRichtext>,
    color: Color,
    modifier: Modifier = Modifier,
    showEmoji: Boolean = true,
) {
    val annotatedString = buildAnnotatedString {
        for (e in richText) {
            when (e) {
                is LinkFlairRichtextText -> {
                    append(e.text)
                }

                is LinkFlairRichtextEmoji if showEmoji -> {
                    appendInlineContent(e.url, e.emoji)
                }

                is LinkFlairRichtextEmoji -> {
                    append(e.emoji)
                }
            }
        }
    }
    val inlineContent: MutableMap<String, InlineTextContent> = mutableMapOf()
    for (e in richText) {
        if (e is LinkFlairRichtextEmoji) {
            inlineContent +=
                e.url to InlineTextContent(
                    placeholder = Placeholder(
                        width = 16.sp,
                        height = 16.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                    )
                ) {
                    AsyncImage(
                        model = e.url,
                        contentDescription = e.emoji,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.height(16.dp)
                    )
                }
        }
    }
    Text(
        annotatedString,
        inlineContent = inlineContent,
        color = color,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall.copy(color = color),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

/** Parse the color string to a Color object using [toColorInt]. If the string is empty or could not be parsed,
 * return the default color.
 *
 * @param color The color string to parse.
 * @param default The default color to use if string is empty.
 * @return The parsed color.*/
fun mapColor(color: String, default: Color = Color.Transparent): Color {
    val colorInt = runCatching { color.toColorInt() }
    return if (colorInt.isSuccess) {
        Color(colorInt.getOrThrow())
    } else when (color) {
        "light" -> Color.White
        "dark" -> Color.Black
        "" -> default
        else -> {
            Log.e("Flair.mapColor", "Could not parse color: $color")
            default
        }
    }
}

/** Invert the color.
 *
 * @param bw If true, restrict output to black and white.
 * @return The inverted color.
 * */
fun Color.invert(bw: Boolean = true): Color {
    return if (bw) {
        if (luminance() > 0.5) {
            Color.Black
        } else {
            Color.White
        }
    } else {
        Color(red = 1f - red, green = 1f - green, blue = 1f - blue, alpha = 1f)
    }
}


@Composable
fun Flair(
    flair: Flair,
    modifier: Modifier = Modifier,
    showColor: Boolean = true,
    showEmoji: Boolean = true,
) {
    if (flair.text.isEmpty() && flair.richText.isEmpty()) return
    val backgroundColor =
        if (showColor) mapColor(flair.backgroundColor, default = Color.DarkGray) else Color.DarkGray
    var textColor =
        if (showColor) mapColor(
            flair.textColor,
            default = backgroundColor.invert(bw = true)
        ) else Color.White
    val l1 = backgroundColor.luminance() + 0.05
    val l2 = textColor.luminance() + 0.05
    val contrastRatio = max(l1, l2) / min(l1, l2)
    if (contrastRatio < 4.5) {
        textColor = textColor.invert()
    }
    val modifier = modifier.cartouche(backgroundColor)
    when (flair.type) {
        "richtext" -> {
            if (flair.richText.isNotEmpty()) {
                FlairRichtext(flair.richText, textColor, modifier, showEmoji)
            } else {
                Text(
                    text = flair.text,
                    style = MaterialTheme.typography.labelSmall.copy(color = textColor),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier,
                )
            }
        }

        "text" -> {
            Text(
                text = flair.text,
                style = MaterialTheme.typography.labelSmall.copy(color = textColor),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier,
            )
        }
    }
}
