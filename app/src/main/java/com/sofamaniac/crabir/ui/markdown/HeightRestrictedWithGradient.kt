package com.sofamaniac.crabir.ui.markdown


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme

/**
 * A layout that restricts its content to [maxHeight]. When the content exceeds
 * that height, it is clipped and a vertical gradient is drawn on top to signal
 * that content continues below.
 *
 * @param maxHeight      The maximum height before clipping kicks in.
 * @param gradientHeight How tall the gradient overlay should be.
 * @param modifier       Modifier applied to the outer container.
 * @param content        The composable content to display.
 *
 * Code fully written by Gemini
 */
@Composable
fun HeightRestrictedWithGradient(
    maxHeight: Dp,
    modifier: Modifier = Modifier,
    gradientHeight: Dp = 64.dp,
    content: @Composable () -> Unit,
) {
    val theme = LocalTheme.current
    val gradientColors = listOf(Color.Transparent, theme.cardBackground)

    val measurePolicy = remember(maxHeight, gradientHeight, gradientColors) {
        object : MeasurePolicy {
            override fun MeasureScope.measure(
                measurables: List<Measurable>,
                constraints: Constraints
            ): MeasureResult {
                val maxHeightPx = maxHeight.roundToPx()
                val contentMeasurable = measurables[0]
                val gradientMeasurable = measurables[1]

                // Measure content with an unconstrained height to know its real size.
                val contentPlaceable =
                    contentMeasurable.measure(constraints.copy(maxHeight = Int.MAX_VALUE))

                val contentWidth = contentPlaceable.width
                val contentHeight = contentPlaceable.height

                val isTaller = contentHeight > maxHeightPx
                val layoutHeight = if (isTaller) maxHeightPx else contentHeight

                val gradientPlaceable = if (isTaller) {
                    val gradientHeightPx = gradientHeight.roundToPx()
                    gradientMeasurable.measure(
                        Constraints.fixed(
                            width = contentWidth,
                            height = gradientHeightPx
                        )
                    )
                } else null

                return layout(width = contentWidth, height = layoutHeight) {
                    contentPlaceable.placeRelative(x = 0, y = 0)
                    gradientPlaceable?.placeRelative(
                        x = 0,
                        y = layoutHeight - gradientPlaceable.height
                    )
                }
            }

            override fun IntrinsicMeasureScope.minIntrinsicHeight(
                measurables: List<IntrinsicMeasurable>,
                width: Int
            ): Int {
                val contentHeight = measurables[0].minIntrinsicHeight(width)
                return minOf(contentHeight, maxHeight.roundToPx())
            }

            override fun IntrinsicMeasureScope.maxIntrinsicHeight(
                measurables: List<IntrinsicMeasurable>,
                width: Int
            ): Int {
                val contentHeight = measurables[0].maxIntrinsicHeight(width)
                return minOf(contentHeight, maxHeight.roundToPx())
            }

            override fun IntrinsicMeasureScope.minIntrinsicWidth(
                measurables: List<IntrinsicMeasurable>,
                height: Int
            ): Int {
                return measurables[0].minIntrinsicWidth(height)
            }

            override fun IntrinsicMeasureScope.maxIntrinsicWidth(
                measurables: List<IntrinsicMeasurable>,
                height: Int
            ): Int {
                return measurables[0].maxIntrinsicWidth(height)
            }
        }
    }

    Layout(
        modifier = modifier.clipToBounds(),
        content = {
            // We wrap content in a Box to ensure it's treated as a single measurable
            Box { content() }
            // The gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gradientHeight)
                    .background(
                        brush = Brush.verticalGradient(gradientColors)
                    )
            )
        },
        measurePolicy = measurePolicy
    )
}
