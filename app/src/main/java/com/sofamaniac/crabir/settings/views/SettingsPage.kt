package com.sofamaniac.crabir.settings.views

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ViewComfy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.ListSelector
import com.sofamaniac.crabir.settings.helper.SettingHeader
import com.sofamaniac.crabir.settings.helper.SwitchTile
import kotlinx.coroutines.launch

@Composable
fun ViewsSettingsPage() {
    val context = LocalContext.current
    val settingsDataStore = remember(context) { context.viewSettingDataStore }
    val viewSettings by settingsDataStore.data.collectAsState(initial = ViewSettings())
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("View settings") }, navigationIcon = {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                ListSelector(
                    options = Views.entries.toList(),
                    selectedOption = viewSettings.defaultView,
                    headlineContent = { Text("Default view") },
                    leadingContent = { Icon(Icons.Default.ViewComfy, contentDescription = null) },
                    onOptionSelected = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(defaultView = target)
                            }
                        }
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Default number of columns") },
                    trailingContent = { Text(viewSettings.defaultColumns.toString()) },
                    supportingContent = {
                        Slider(
                            value = viewSettings.defaultColumns.toFloat(),
                            valueRange = 1f..3f,
                            steps = 1,
                            onValueChange = { target ->
                                scope.launch {
                                    settingsDataStore.updateData {
                                        it.copy(defaultColumns = target.toInt())
                                    }
                                }
                            }
                        )
                    }
                )
            }

            item {
                SwitchTile(
                    checked = viewSettings.rememberView,
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(rememberView = target)
                            }
                        }
                    },
                    headlineContent = { Text("Remember view") },
                    supportingContent = { Text("Each community will remember the last view selected for that community") }
                )
            }
            item {
                SwitchTile(
                    checked = viewSettings.prefixCommunity,
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(prefixCommunity = target)
                            }
                        }
                    },
                    headlineContent = { Text("Communities start with r/") },
                )
            }
            item { SettingHeader(stringResource(Views.Card.toStringResource())) }
            item {
                SwitchTile(
                    checked = viewSettings.cardSettings.roundedCorners,
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(cardSettings = it.cardSettings.copy(roundedCorners = target))
                            }
                        }
                    },
                    headlineContent = { Text("Rounded corners") },
                )
            }
            item {
                SwitchTile(
                    checked = viewSettings.cardSettings.enableFullHeightImage,
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(cardSettings = it.cardSettings.copy(enableFullHeightImage = target))
                            }
                        }
                    },
                    headlineContent = { Text("Enable full height image") },
                    supportingContent = { Text("Disable for fixed height images") }
                )
            }
            item {
                SwitchTile(
                    checked = viewSettings.cardSettings.thumbnailForLinkPreview,
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(cardSettings = it.cardSettings.copy(thumbnailForLinkPreview = target))
                            }
                        }
                    },
                    headlineContent = { Text("Show thumbnail for link preview") },
                    supportingContent = { Text("Disable to user large image preview") }
                )
            }
            item {
                SwitchTile(
                    checked = viewSettings.cardSettings.enableTextPreview,
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(cardSettings = it.cardSettings.copy(enableTextPreview = target))
                            }
                        }
                    },
                    headlineContent = { Text("Enable text preview") },
                )
            }

            item {
                ListItem(
                    leadingContent = { Spacer(modifier = Modifier.size(24.dp)) },
                    headlineContent = { Text("Number of lines") },
                    trailingContent = {
                        TextField(
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            value = viewSettings.cardSettings.maxLines.toString(),
                            singleLine = true,
                            onValueChange = { target ->
                                scope.launch {
                                    var target = target.toIntOrNull() ?: 5
                                    target = target.coerceIn(1, 100)
                                    settingsDataStore.updateData {
                                        Log.d("ViewsSettingsPage", "maxLines: $target")
                                        it.copy(cardSettings = it.cardSettings.copy(maxLines = target))
                                    }
                                }
                            }
                        )
                    }
                )
            }
        }
    }
}