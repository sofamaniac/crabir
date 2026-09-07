package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ListItemElevation
import androidx.compose.material3.ListItemShapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sofamaniac.crabir.settings.theme.rememberListItemColors
import androidx.compose.material3.ListItem as BaseListItem

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    overlineContent: (@Composable () -> Unit)? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    verticalAlignement: Alignment.Vertical = ListItemDefaults.verticalAlignment(),
    colors: ListItemColors = rememberListItemColors(),
    shapes: ListItemShapes = ListItemDefaults.shapes(),
    elevation: ListItemElevation = ListItemDefaults.elevation(),
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    BaseListItem(
        modifier = modifier,
        enabled = enabled,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        verticalAlignment = verticalAlignement,
        shapes = shapes,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun ListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    overlineContent: (@Composable () -> Unit)? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    verticalAlignement: Alignment.Vertical = ListItemDefaults.verticalAlignment(),
    colors: ListItemColors = rememberListItemColors(),
    shapes: ListItemShapes = ListItemDefaults.shapes(),
    elevation: ListItemElevation = ListItemDefaults.elevation(),
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    BaseListItem(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        verticalAlignment = verticalAlignement,
        shapes = shapes,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun ListItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    overlineContent: (@Composable () -> Unit)? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    verticalAlignement: Alignment.Vertical = ListItemDefaults.verticalAlignment(),
    colors: ListItemColors = rememberListItemColors(),
    shapes: ListItemShapes = ListItemDefaults.shapes(),
    elevation: ListItemElevation = ListItemDefaults.elevation(),
    contentPadding: PaddingValues = ListItemDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    BaseListItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        verticalAlignment = verticalAlignement,
        shapes = shapes,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        content = content
    )
}
