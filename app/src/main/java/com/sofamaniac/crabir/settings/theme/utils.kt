package com.sofamaniac.crabir.settings.theme

import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.LocalTheme

@Composable
fun rememberTopAppBarColors(): TopAppBarColors {
    val theme = LocalTheme.current
    return TopAppBarColors(
        containerColor = theme.toolbarBackground,
        scrolledContainerColor = theme.toolbarBackground,
        navigationIconContentColor = theme.toolbarText,
        titleContentColor = theme.toolbarText,
        actionIconContentColor = theme.toolbarText,
        subtitleContentColor = theme.toolbarText
    )
}

@Composable
fun rememberListItemColors(): ListItemColors {
    val theme = LocalTheme.current
    val default = ListItemDefaults.colors()
    return default.copy(containerColor = theme.cardBackground)
}
