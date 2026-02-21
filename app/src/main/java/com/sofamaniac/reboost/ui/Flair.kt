/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.reboost.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.sofamaniac.reboost.data.remote.dto.LinkFlairRichtext
import com.sofamaniac.reboost.data.remote.dto.LinkFlairRichtextEmoji
import com.sofamaniac.reboost.data.remote.dto.LinkFlairRichtextText
import com.sofamaniac.reboost.domain.model.Flair
import kotlin.math.max
import kotlin.math.min


@Composable
fun FlairRichtext(richText: List<LinkFlairRichtext>, color: Color) {
    for (i in richText) {
        when (i) {
            is LinkFlairRichtextText -> {
                Text(i.text, style = MaterialTheme.typography.labelSmall.copy(color = color))
            }

            is LinkFlairRichtextEmoji -> {
                AsyncImage(
                    model = i.url,
                    contentDescription = i.emoji,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}

/** Parse the color string to a Color object using [String.toColorInt]. If the string is empty or could not be parsed,
 * return the default color.
 *
 * @param color The color string to parse.
 * @param default The default color to use if string is empty.
 * @return The parsed color.*/
fun mapColor(color: String, default: Color = Color.Transparent): Color {
    return try {
        Color(color.toColorInt())
    } catch (e: Exception) {
        when (color) {
            "light" -> Color.White
            "dark" -> Color.Black
            "" -> default
            else -> {
                Log.e("Flair.mapColor", "Could not parse color: $color")
                default
            }
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
fun Flair(flair: Flair) {
    if (flair.text.isEmpty() && flair.richText.isEmpty()) return
    val backgroundColor = mapColor(flair.backgroundColor, default = Color.DarkGray)
    var textColor = mapColor(flair.textColor, default = backgroundColor.invert(bw = true))
    val l1 = backgroundColor.luminance() + 0.05
    val l2 = textColor.luminance() + 0.05
    val contrastRatio = max(l1, l2) / min(l1, l2)
    if (contrastRatio < 4.5) {
        textColor = textColor.invert()
    }
    Cartouche(backgroundColor = backgroundColor) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            when (flair.type) {
                "richtext" -> {
                    if (flair.richText.isNotEmpty()) {
                        FlairRichtext(flair.richText, textColor)
                    } else {
                        Text(
                            text = flair.text,
                            style = MaterialTheme.typography.labelSmall.copy(color = textColor)
                        )
                    }
                }

                "text" -> {
                    Text(
                        text = flair.text,
                        style = MaterialTheme.typography.labelSmall.copy(color = textColor)
                    )
                }
            }
        }
    }
}