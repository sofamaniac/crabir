package com.sofamaniac.crabir.settings.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.CloseButton
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.ThemedDialog
import com.sofamaniac.crabir.ui.protectedTouch
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeEditor() {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }
    val themeSettings by themeDataStore.data.collectAsState(
        initial = ThemeSettings.DEFAULT,
    )
    val mode = themeSettings.currentMode()

    val theme =
        if (mode == ThemeMode.Dark) themeSettings.dark else themeSettings.light
    val parentTheme = themeSettings.getParentTheme(mode)
    var activeColorField by remember { mutableStateOf<ColorFields?>(null) }
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    var showSavedThemesDialog by remember { mutableStateOf(false) }

    fun updateColor(field: ColorFields, color: Color) {
        val newTheme = theme.updateFieldValue(field, color)
        scope.launch {
            themeDataStore.updateData {
                if (themeSettings.mode == ThemeMode.Dark) {
                    themeSettings.copy(dark = newTheme)
                } else {
                    themeSettings.copy(light = newTheme)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.theme_editor)) }, navigationIcon = {
                CloseButton { navController?.popBackStack() }
            })
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
                    updateColor(field, currentColor)
                    activeColorField = null
                }
            )
        }
        Column(modifier = Modifier.padding(paddingValues)) {
            ThemePreviewer(modifier = Modifier.padding(8.dp)) { activeColorField = it }
            LazyColumn {
                item {
                    ListItem(content = {
                        TextButton(onClick = {
                            scope.launch {
                                themeDataStore.updateData {
                                    if (themeSettings.mode == ThemeMode.Dark) {
                                        themeSettings.copy(dark = parentTheme)
                                    } else {
                                        themeSettings.copy(light = parentTheme)
                                    }
                                }
                            }
                        }) {
                            Text(stringResource(R.string.reset_theme))
                        }
                    })
                }
                item {
                    ListItem(
                        content = {
                            TextButton(onClick = { showSavedThemesDialog = true }) {
                                Text(stringResource(R.string.saved_themes))
                            }
                        },
                        trailingContent = {
                            val totalThemes =
                                themeSettings.collections.dark.size + themeSettings.collections.light.size
                            Text(stringResource(R.string.total_themes, totalThemes))
                        })
                }
                items(ColorFields.entries.size, key = { ColorFields.entries[it] }) { field ->
                    val field = ColorFields.entries[field]
                    ListItem(
                        leadingContent = {
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
                        content = { Text(field.name) },
                        trailingContent = {
                            val currentColor = theme.getFieldValue(field)
                            val parentColor = parentTheme.getFieldValue(field)
                            if (currentColor != parentColor) {
                                IconButton(onClick = {
                                    updateColor(field, parentColor)
                                }) {
                                    Icon(
                                        Icons.AutoMirrored.Default.Undo,
                                        contentDescription = stringResource(R.string.reset_color)
                                    )
                                }
                            }
                        },
                        modifier = Modifier.clickable {
                            activeColorField = field
                        },
                    )
                }
            }
        }
    }
    if (showSavedThemesDialog) {
        SavedThemesDialog { showSavedThemesDialog = false }
    }
}

@Composable
fun SavedThemesDialog(onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }
    val themeSettings by themeDataStore.data.collectAsState(
        initial = ThemeSettings.DEFAULT,
    )
    val mode = themeSettings.currentMode()
    val collection = themeSettings.collections.dark + themeSettings.collections.light
    val scope = rememberCoroutineScope()
    ThemedDialog(onDismissRequest) {
        for (theme in collection) {
            ListItem(
                content = {
                    Column {
                        Text(theme.key)
                        ThemePreviewer(theme = theme.value) { }
                        HorizontalDivider()
                    }
                },
                modifier = Modifier.protectedTouch {
                    scope.launch {
                        themeDataStore.updateData {
                            if (mode == ThemeMode.Dark) {
                                themeSettings.copy(darkParentTheme = theme.key)
                            } else {
                                themeSettings.copy(lightParentTheme = theme.key)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ThemePreviewer(
    modifier: Modifier = Modifier,
    theme: CrabirTheme = rememberAppTheme(),
    setActiveField: (ColorFields) -> Unit,
) {
    CompositionLocalProvider(LocalTheme provides theme) {
        ThemedCard(modifier = modifier) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.theme_post_title), modifier = Modifier.clickable {
                        setActiveField(ColorFields.PostTitle)
                    }, color = theme.postTitle)
                    Text(stringResource(R.string.theme_read), modifier = Modifier.clickable {
                        setActiveField(ColorFields.ReadPost)
                    }, color = theme.readPost)
                    Text(
                        stringResource(R.string.theme_announcement),
                        modifier = Modifier.clickable {
                            setActiveField(ColorFields.Announcement)
                        },
                        color = theme.announcement
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.theme_community), modifier = Modifier.clickable {
                        setActiveField(ColorFields.Highlight)
                    }, color = theme.highlight)
                    Text(
                        stringResource(R.string.theme_secondary_text),
                        modifier = Modifier.clickable {
                            setActiveField(ColorFields.SecondaryText)
                        },
                        color = theme.secondaryText
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        stringResource(R.string.theme_content_text),
                        modifier = Modifier.clickable {
                            setActiveField(ColorFields.ContentColor)
                        },
                        color = theme.contentColor
                    )
                    Text(stringResource(R.string.theme_link), modifier = Modifier.clickable {
                        setActiveField(ColorFields.LinkColor)
                    }, color = theme.linkColor)
                    Text(stringResource(R.string.theme_downvote), modifier = Modifier.clickable {
                        setActiveField(ColorFields.Downvote)
                    }, color = theme.downvote)
                }
            }
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
    ThemedDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        roundedCorners = true,
    ) {
        ColorPicker(color, onColorChange, advancedMode)
        Row {
            TextButton(onClick = { advancedMode = !advancedMode }) {
                if (advancedMode) {
                    Text(stringResource(R.string.color_picker_presets))
                } else {
                    Text(stringResource(R.string.color_picker_custom))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(onClick = applyChanges) {
                Text(stringResource(R.string.confirm))
            }
        }
    }
}