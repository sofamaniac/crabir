package com.sofamaniac.crabir.settings.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.sofamaniac.crabir.R

@Composable
fun ColorPicker(color: Color, onValueChange: (Color) -> Unit, advancedMode: Boolean = false) {
    if (advancedMode) {
        AdvancedColorPicker(color, onValueChange)
    } else {
        SimpleColorPicker(color, onValueChange)
    }
}

fun generateColorShades(
    hue: Float,
    saturation: Float = 1f,
    lightnessRange: ClosedFloatingPointRange<Float> = 0.25f..0.85f,
    columns: Int = 4,
    rows: Int = 3,
): List<Color> {
    return (0 until rows * columns).map { i ->
        val t = i.toFloat() / (rows * columns - 1)
        val lightness = lerp(lightnessRange.start, lightnessRange.endInclusive, t)
        Color.hsl(hue, saturation, lightness)
    }
}

@Composable
fun SimpleColorPicker(color: Color, onValueChange: (Color) -> Unit) {
    val bases =
        listOf(4f, 340f, 290f, 230f, 207f, 200f, 187f, 174f, 123f, 88f, 66f, 54f, 45f, 36f, 15f)

    var presets by remember { mutableStateOf(listOf<Color>()) }


    LazyVerticalGrid(
        columns = GridCells.FixedSize(64.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (presets.isEmpty()) {
            items(bases.size) {
                val currentColor = Color.hsl(bases[it], 1f, 0.6f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(64.dp)
                        .background(currentColor)
                        .clip(CircleShape)
                        .clickable {
                            presets = generateColorShades(bases[it])
                        }

                ) {
                    if (color == currentColor) {
                        Icon(Icons.Default.Check, contentDescription = null)
                    }
                }
            }
        } else {
            items(presets.size) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(64.dp)
                        .background(presets[it])
                        .clip(CircleShape)
                        .clickable {
                            onValueChange(presets[it])
                            presets = listOf()
                        }
                )
            }

        }
    }
}

@Composable
fun AdvancedColorPicker(color: Color, onValueChange: (Color) -> Unit) {

    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.select_a_color))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(color)
        )
        Text(
            color.toHexString(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Slider(
            color.alpha,
            steps = 256,
            onValueChange = {
                onValueChange(color.copy(alpha = it))
            })
        Slider(
            color.red,
            steps = 256,
            onValueChange = {
                onValueChange(color.copy(red = it))
            })
        Slider(
            color.green,
            steps = 256,
            onValueChange = {
                onValueChange(color.copy(green = it))
            })
        Slider(
            color.blue,
            steps = 256,
            onValueChange = {
                onValueChange(color.copy(blue = it))
            })
    }
}

fun Color.toHexString(): String {
    val redString = (red * 255f).toInt().toHexString().substring(6, 8)
    val greenString = (green * 255f).toInt().toHexString().substring(6, 8)
    val blueString = (blue * 255f).toInt().toHexString().substring(6, 8)
    return "$redString$greenString$blueString"
}
