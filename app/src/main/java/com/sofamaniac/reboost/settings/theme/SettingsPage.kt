package com.sofamaniac.reboost.settings.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ThemeEditorRoute
import com.sofamaniac.reboost.settings.ui.ListSelector
import com.sofamaniac.reboost.settings.ui.SwitchTile
import kotlinx.coroutines.launch

@Composable
fun ThemeSettingsPage() {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }

    val settings by themeDataStore.data.collectAsState(initial = ThemeSettings.DEFAULT)
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current!!

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ListSelector(
                leadingContent = {
                    Icon(
                        Icons.Default.Brightness6,
                        contentDescription = "Dark mode"
                    )
                },
                headlineContent = { Text("Theme") },
                supportingContent = { Text("Choose your theme") },
                options = ThemeMode.entries.toList(),
                selectedOption = {
                    Text(
                        stringResource(settings.mode.toStringResource()),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                onOptionSelected = { target ->
                    scope.launch {
                        themeDataStore.updateData {
                            it.copy(mode = target)
                        }
                    }
                },
                optionLabel = { stringResource(id = it.toStringResource()) },
                modifier = Modifier.fillMaxWidth()
            )
            SwitchTile(
                headlineContent = { Text("Dynamic color") },
                checked = settings.dynamicColor,
                onCheckedChange = { target ->
                    scope.launch {
                        themeDataStore.updateData {
                            it.copy(dynamicColor = target)
                        }
                    }
                }
            )
            ConditionalListItem(
                text = "Edit Colors",
                enabled = !settings.dynamicColor,
                onClick = {
                    navController.navigate(ThemeEditorRoute)
                },
                icon = Icons.Default.Palette
            )
        }
    }
}

@Composable
fun ConditionalListItem(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
) {
    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) // M3 disabled alpha
    }

    ListItem(
        modifier = modifier.clickable(enabled = enabled, onClick = onClick),
        headlineContent = {
            Text(text, color = contentColor)
        },
        leadingContent = {
            icon?.let {
                Icon(imageVector = it, contentDescription = null, tint = contentColor)
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = null,
                tint = contentColor
            )
        }
    )
}
