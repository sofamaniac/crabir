package com.sofamaniac.crabir.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.sofamaniac.crabir.LocalTheme

@Composable
fun ThemedCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    highlight: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val theme = LocalTheme.current
    val background = if (highlight) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        theme.cardBackground
    }
    Card(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        border = border,
        content = content,
        colors = CardDefaults.cardColors().copy(containerColor = background)
    )
}

@Composable
fun ThemedCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val theme = LocalTheme.current
    Card(
        modifier = modifier,
        shape = shape,
        elevation = elevation,
        border = border,
        content = content,
        colors = CardDefaults.cardColors().copy(containerColor = theme.cardBackground),
        onClick = onClick
    )
}
