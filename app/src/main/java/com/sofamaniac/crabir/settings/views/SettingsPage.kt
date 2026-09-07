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
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.sofamaniac.crabir.navigation.ViewManagerRoute
import com.sofamaniac.crabir.settings.helper.ListSelector
import com.sofamaniac.crabir.settings.helper.SettingHeader
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.components.ListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ViewsSettingsPage() {
    val context = LocalContext.current
    val settingsDataStore = remember(context) { context.viewSettingDataStore }
    val viewSettingsOpt by settingsDataStore.data.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.view_settings)) }, navigationIcon = {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            })
        }
    ) { innerPadding ->
        if (viewSettingsOpt == null) return@Scaffold
        val viewSettings = viewSettingsOpt!!
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                ListSelector(
                    options = Views.entries.toList(),
                    selectedOption = viewSettings.defaultView,
                    headlineContent = { Text(stringResource(R.string.default_view)) },
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
                    enabled = true,
                    leadingContent = { Spacer(modifier = Modifier.size(24.dp)) },
                    content = { Text(stringResource(R.string.default_number_of_columns)) },
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
                    headlineContent = { Text(stringResource(R.string.remember_view_main)) },
                    supportingContent = { Text(stringResource(R.string.remember_view_supporting)) }
                )
            }
            item {
                ListItem(
                    leadingContent = { Spacer(modifier = Modifier.size(24.dp)) },
                    onClick = { navController?.navigate(ViewManagerRoute) },
                    enabled = viewSettings.rememberView
                ) {
                    Text(stringResource(R.string.manage_views))
                }
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
                    headlineContent = { Text(stringResource(R.string.communities_start_with_r)) },
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
                    headlineContent = { Text(stringResource(R.string.rounded_corners)) },
                )
            }
            item {
                ListSelector(
                    options = ImageHeight.entries.toList(),
                    selectedOption = viewSettings.cardSettings.imageHeight,
                    headlineContent = { Text(stringResource(R.string.image_height)) },
                    onOptionSelected = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(cardSettings = it.cardSettings.copy(imageHeight = target))
                            }
                        }
                    },
                    optionLabel = {
                        when (it) {
                            ImageHeight.Full -> stringResource(R.string.image_height_full)
                            ImageHeight.Fixed -> stringResource(R.string.image_height_fixed)
                            ImageHeight.Screen -> stringResource(R.string.image_height_limit)
                        }
                    }
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
                    headlineContent = { Text(stringResource(R.string.show_thumbnail_for_link_preview)) },
                    supportingContent = { Text(stringResource(R.string.show_thumbnail_for_link_preview_support)) }
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
                    headlineContent = { Text(stringResource(R.string.enable_text_preview)) },
                )
            }

            item {
                ListItem(
                    leadingContent = { Spacer(modifier = Modifier.size(24.dp)) },
                    content = {
                        TextField(
                            enabled = viewSettings.cardSettings.enableTextPreview,
                            label = { Text(stringResource(R.string.number_of_lines_main)) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            value = viewSettings.cardSettings.maxLines.toString(),
                            supportingText = { Text(stringResource(R.string.number_of_lines_support)) },
                            suffix = { Text(stringResource(R.string.lines)) },
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
