package com.sofamaniac.crabir.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.LocalTheme

@Composable
fun ThemedScaffold(
    modifier: Modifier = Modifier,
    containerColor: Color = LocalTheme.current.background,
    contentColor: Color = contentColorFor(containerColor),
    topBar: (@Composable () -> Unit) = {},
    bottomBar: (@Composable () -> Unit) = {},
    floatingActionButton: (@Composable () -> Unit) = {},
    floatinActionButtonPosition: FabPosition = FabPosition.End,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable ((PaddingValues) -> Unit),
) {
    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        Scaffold(
            containerColor = containerColor,
            contentColor = contentColor,
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            topBar = topBar,
            bottomBar = bottomBar,
            modifier = modifier,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatinActionButtonPosition,
            content = content
        )
    }
}
