package com.sofamaniac.crabir.settings.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SwitchTile(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable () -> Unit = {
        Spacer(modifier = Modifier.size(24.dp))
    },
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    ListItem(
        modifier = Modifier.selectable(
            selected = checked,
            role = Role.Switch,
            enabled = enabled,
            onClick = {
                onCheckedChange(!checked)
            }
        ),
        headlineContent = { headlineContent() },
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = {
            Switch(
                enabled = enabled,
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}