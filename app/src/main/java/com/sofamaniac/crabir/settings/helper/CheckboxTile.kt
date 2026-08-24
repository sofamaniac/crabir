package com.sofamaniac.crabir.settings.helper

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.ui.ThemedCheckbox

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CheckboxTile(
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
            role = Role.Checkbox,
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
            ThemedCheckbox(
                enabled = enabled,
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        },
    )
}
