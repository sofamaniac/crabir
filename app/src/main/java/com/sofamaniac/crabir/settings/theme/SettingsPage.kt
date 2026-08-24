package com.sofamaniac.crabir.settings.theme

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.ThemeEditorRoute
import com.sofamaniac.crabir.settings.helper.ListSelector
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.BackButton
import com.sofamaniac.crabir.ui.ThemedDialog
import kotlinx.coroutines.launch

@Composable
fun ThemeSettingsPage() {
    val context = LocalContext.current
    val themeDataStore = remember(context) { context.themeDataStore }

    val settings = rememberThemeSettings()
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current!!
    val startTimeState = rememberTimePickerState(
        initialHour = settings.lightModeStartTime,
        initialMinute = 0,
    )
    val endTimeState = rememberTimePickerState(
        initialHour = settings.lightModeEndTime,
        initialMinute = 0,
    )

    var showStartTimeDialog by remember { mutableStateOf(false) }
    var showEndTimeDialog by remember { mutableStateOf(false) }

    if (showStartTimeDialog) {
        TimePickerDialog(
            onDismiss = { showStartTimeDialog = false },
            onConfirm = {
                scope.launch {
                    themeDataStore.updateData {
                        it.copy(lightModeStartTime = startTimeState.hour)
                    }
                }
                showStartTimeDialog = false
            }
        ) {
            TimePicker(state = startTimeState)
        }
    }
    if (showEndTimeDialog) {
        TimePickerDialog(
            onDismiss = { showEndTimeDialog = false },
            onConfirm = {
                scope.launch {
                    themeDataStore.updateData {
                        it.copy(lightModeEndTime = endTimeState.hour)
                    }
                }
                showEndTimeDialog = false
            }
        ) {
            TimePicker(state = endTimeState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.theme_settings)) }, navigationIcon = {
                BackButton {
                    navController.popBackStack()
                }
            })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ListSelector(
                leadingContent = {
                    Icon(
                        Icons.Default.Brightness6,
                        contentDescription = null,
                    )
                },
                headlineContent = { Text(stringResource(R.string.theme)) },
                options = ThemeMode.entries.toList(),
                selectedOption = settings.mode,
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.dynamic_color)) },
                    checked = settings.dynamicColor,
                    onCheckedChange = { target ->
                        scope.launch {
                            themeDataStore.updateData {
                                it.copy(dynamicColor = target)
                            }
                        }
                    }
                )
            }
            ListItem(
                content = { Text(stringResource(R.string.edit_colors)) },
                enabled = !settings.dynamicColor || Build.VERSION.SDK_INT < Build.VERSION_CODES.S,
                onClick = {
                    navController.navigate(ThemeEditorRoute)
                },
                leadingContent = {
                    Icon(Icons.Default.Palette, contentDescription = null)
                }
            )
            if (settings.mode == ThemeMode.Scheduled) {
                ListItem(
                    content = {
                        Text(stringResource(R.string.light_mode_start_time))
                    },
                    trailingContent = {
                        val hour = "%02d".format(endTimeState.hour)
                        val minute = "%02d".format(endTimeState.minute)
                        Text(
                            "${hour}:${minute}"
                        )
                    },
                    modifier = Modifier.clickable {
                        showStartTimeDialog = true
                    }
                )
                ListItem(
                    content = {
                        Text(stringResource(R.string.light_mode_end_time))
                    },
                    trailingContent = {
                        val hour = "%02d".format(endTimeState.hour)
                        val minute = "%02d".format(endTimeState.minute)
                        Text(
                            "${hour}:${minute}"
                        )
                    },
                    modifier = Modifier.clickable {
                        showEndTimeDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit,
) {
    ThemedDialog(
        onDismissRequest = onDismiss,
        cancel = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.cancel))
            }
        },
        confirm = {
            TextButton(onClick = { onConfirm() }) {
                Text(stringResource((R.string.confirm)))
            }
        },
        content = { content() }
    )
}