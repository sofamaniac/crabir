package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ThemedDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    roundedCorners: Boolean = false,
    title: (@Composable () -> Unit)? = null,
    cancel: (@Composable () -> Unit)? = null,
    confirm: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest) {
        ThemedCard(modifier = modifier, roundedCorners) {
            Column(modifier = Modifier.padding(8.dp)) {
                title?.invoke()
                if (title != null) Spacer(modifier = Modifier.height(8.dp))
                content()
                if (cancel != null || confirm != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        cancel?.invoke()
                        Spacer(modifier = Modifier.weight(1f))
                        confirm?.invoke()
                    }
                }
            }
        }
    }
}