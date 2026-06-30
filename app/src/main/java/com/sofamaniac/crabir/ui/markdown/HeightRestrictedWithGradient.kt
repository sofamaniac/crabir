package com.sofamaniac.crabir.ui.markdown


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme

/**
 * A layout that restricts its content to [maxHeight]. When the content exceeds
 * that height, it is clipped and a vertical gradient is drawn on top to signal
 * that content continues below.
 *
 * @param maxHeight      The maximum height before clipping kicks in.
 * @param gradientColors Colors used for the bottom fade (top → bottom).
 *                       Defaults to transparent → black.
 * @param gradientHeight How tall the gradient overlay should be.
 * @param modifier       Modifier applied to the outer container.
 * @param content        The composable content to display.
 *
 * Code fully written by Claude.ai
 */
@Composable
fun HeightRestrictedWithGradient(
    maxHeight: Dp,
    modifier: Modifier = Modifier,
    //gradientColors: List<Color> = listOf(Color.Transparent, Color.Black),
    gradientHeight: Dp = 64.dp,
    content: @Composable () -> Unit,
) {
    val theme = LocalTheme.current
    val gradientColors = listOf(Color.Transparent, theme.cardBackground)
    SubcomposeLayout(modifier = modifier.clipToBounds()) { constraints ->
        val maxHeightPx = maxHeight.roundToPx()

        // 1. Measure content with an unconstrained height so we know its real size.
        val contentPlaceables = subcompose("content", content).map { measurable ->
            measurable.measure(constraints.copy(maxHeight = Int.MAX_VALUE))
        }

        val contentWidth = contentPlaceables.maxOfOrNull { it.width } ?: 0
        val contentHeight = contentPlaceables.maxOfOrNull { it.height } ?: 0

        val isTaller = contentHeight > maxHeightPx
        val layoutHeight = if (isTaller) maxHeightPx else contentHeight

        // 2. Measure + place the gradient overlay only when content is taller.
        val gradientPlaceables = if (isTaller) {
            val gradientHeightPx = gradientHeight.roundToPx()
            subcompose("gradient") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(gradientHeight)
                        .background(
                            brush = Brush.verticalGradient(gradientColors)
                        )
                )
            }.map { measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = contentWidth,
                        maxWidth = contentWidth,
                        minHeight = gradientHeightPx,
                        maxHeight = gradientHeightPx,
                    )
                )
            }
        } else emptyList()

        layout(width = contentWidth, height = layoutHeight) {
            // Place content, clipped naturally by the layout's own height.
            contentPlaceables.forEach { it.placeRelative(x = 0, y = 0) }

            // Pin the gradient to the bottom of the visible area.
            gradientPlaceables.forEach { placeable ->
                placeable.placeRelative(
                    x = 0,
                    y = layoutHeight - placeable.height,
                )
            }
        }
    }
}