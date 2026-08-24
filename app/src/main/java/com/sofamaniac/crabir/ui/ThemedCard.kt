package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import com.sofamaniac.crabir.LocalTheme

@Composable
fun ThemedCard(
    modifier: Modifier = Modifier,
    //shape: Shape = CardDefaults.shape,
    roundedCorners: Boolean = false,
    elevation: CardElevation = CardDefaults.cardElevation(),
    bottomBorder: Boolean = false,
    highlight: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val theme = LocalTheme.current
    val background = if (highlight) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        theme.cardBackground
    }
    val shape = if (roundedCorners) {
        CardDefaults.shape
    } else {
        RoundedCornerShape(0)
    }
    val shadowColor = contentColorFor(theme.background).copy(alpha = 0.3f)
    val modifier = if (!bottomBorder) modifier else {
        modifier.drawBehind {
            val strokeWidth = 2 * density
            val y = size.height //- strokeWidth / 2
            drawLine(
                color = shadowColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        }
    }
    if (onClick != null) {
        Card(
            modifier = modifier,
            shape = shape,
            onClick = onClick,
            elevation = elevation,
            //border = border,
            content = content,
            colors = CardDefaults.cardColors().copy(containerColor = background)
        )
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            elevation = elevation,
            //border = border,
            content = content,
            colors = CardDefaults.cardColors().copy(containerColor = background)
        )
    }
}

//@Composable
//fun ThemedCard(
//    modifier: Modifier = Modifier,
//    shape: Shape = CardDefaults.shape,
//    elevation: CardElevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//    border: BorderStroke? = null,
//    onClick: () -> Unit,
//    content: @Composable ColumnScope.() -> Unit,
//) {
//    val theme = LocalTheme.current
//    Card(
//        modifier = modifier,
//        shape = shape,
//        elevation = elevation,
//        border = border,
//        colors = CardDefaults.cardColors().copy(containerColor = theme.cardBackground),
//        onClick = onClick,
//        content = content,
//    )
//}