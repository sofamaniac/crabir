package com.sofamaniac.crabir.settings.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeEditor() {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }
    val themeSettings by themeDataStore.data.collectAsState(
        initial = ThemeSettings.DEFAULT,
    )
    val mode = when (themeSettings.mode) {
        ThemeMode.System, ThemeMode.Scheduled -> if (isSystemInDarkTheme()) ThemeMode.Dark else ThemeMode.Light
        else -> themeSettings.mode
    }

    val theme =
        if (mode == ThemeMode.Dark) themeSettings.dark else themeSettings.light
    var activeColorField by remember { mutableStateOf<ColorFields?>(null) }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Theme editor") })
        },
    ) { paddingValues ->
        activeColorField?.let { field ->
            var currentColor by remember { mutableStateOf(theme.getFieldValue(field)) }
            ColorPickerDialogue(
                currentColor,
                onColorChange = {
                    currentColor = it
                }, onDismissRequest = {
                    activeColorField = null
                },
                applyChanges = {
                    scope.launch {
                        themeDataStore.updateData {
                            if (themeSettings.mode == ThemeMode.Dark) {
                                themeSettings.copy(
                                    dark = theme.updateFieldValue(
                                        field,
                                        currentColor
                                    )
                                )
                            } else {
                                themeSettings.copy(
                                    light = theme.updateFieldValue(
                                        field,
                                        currentColor
                                    )
                                )
                            }
                        }
                        activeColorField = null
                    }
                }
            )
        }
        Column(modifier = Modifier.padding(paddingValues)) {
            ThemePreviewer { activeColorField = it }
            TextButton(onClick = {
                scope.launch {
                    themeDataStore.updateData { ThemeSettings.DEFAULT }
                }
            }) {
                Text("Reset to default")
            }
            LazyColumn {
                items(ColorFields.entries.size, key = { ColorFields.entries[it] }) { field ->
                    val field = ColorFields.entries[field]
                    ListItem(
                        headlineContent = { Text(field.name) },
                        trailingContent = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(theme.getFieldValue(field))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            4.dp
                                        )
                                    )
                            )
                        },
                        modifier = Modifier.clickable {
                            activeColorField = field
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ThemePreviewer(setActiveField: (ColorFields) -> Unit) {
    val theme = rememberAppTheme()
    if (theme == null) return
    Card(
        modifier = Modifier.padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.cardBackground)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Post title", modifier = Modifier.clickable {
                setActiveField(ColorFields.PostTitle)
            }, color = theme.postTitle)
            Text("Read", modifier = Modifier.clickable {
                setActiveField(ColorFields.ReadPost)
            }, color = theme.readPost)
            Text("Announcement", modifier = Modifier.clickable {
                setActiveField(ColorFields.Announcement)
            }, color = theme.announcement)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Community", modifier = Modifier.clickable {
                setActiveField(ColorFields.Highlight)
            }, color = theme.highlight)
            Text("Secondary text", modifier = Modifier.clickable {
                setActiveField(ColorFields.SecondaryText)
            }, color = theme.secondaryText)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Content text", modifier = Modifier.clickable {
                setActiveField(ColorFields.ContentColor)
            }, color = theme.contentColor)
            Text("Link", modifier = Modifier.clickable {
                setActiveField(ColorFields.LinkColor)
            }, color = theme.linkColor)
            Text("Downvote", modifier = Modifier.clickable {
                setActiveField(ColorFields.Downvote)
            }, color = theme.downvote)
        }
    }
}


@Composable
fun ColorPickerDialogue(
    color: Color,
    onColorChange: (Color) -> Unit,
    onDismissRequest: () -> Unit,
    applyChanges: () -> Unit,
) {
    var advancedMode by remember { mutableStateOf(true) }
    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        ) {
            ColorPicker(color, onColorChange, advancedMode)
            Row() {
                TextButton(onClick = { advancedMode = !advancedMode }) {
                    if (advancedMode) {
                        Text("Presets")
                    } else {
                        Text("Custom")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }
                TextButton(onClick = applyChanges) {
                    Text("Confirm")
                }
            }
        }
    }
}