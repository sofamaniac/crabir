package com.sofamaniac.crabir.settings.helper

import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.ListItemSpacer
import com.sofamaniac.crabir.ui.components.ThemedSwitch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SwitchTile(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable () -> Unit = {
        ListItemSpacer()
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
        enabled = enabled,
        content = { headlineContent() },
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = {
            ThemedSwitch(
                enabled = enabled,
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        },
    )
}
