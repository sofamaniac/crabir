package com.sofamaniac.crabir.ui

import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import com.sofamaniac.crabir.LocalTheme

@Composable
fun ThemedSwitch(enabled: Boolean = true, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val theme = LocalTheme.current
    Switch(
        enabled = enabled,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors().copy(checkedThumbColor = theme.postTitle)
    )
}