package com.sofamaniac.crabir.settings.data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.sofamaniac.crabir.LocalSnackBarHost
import com.sofamaniac.crabir.domain.model.Quality
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.ListSelector
import com.sofamaniac.crabir.settings.helper.SettingHeader
import com.sofamaniac.crabir.settings.theme.BackButton
import kotlinx.coroutines.launch

@Composable
fun DataSettingsPage() {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val dataSettingsStore = remember(context) { context.dataSettingsStore }
    val dataSettings by dataSettingsStore.data.collectAsState(DataSettingsDefault.defaultDataSettings)
    val scope = rememberCoroutineScope()
    CompositionLocalProvider(LocalSnackBarHost provides snackbarHostState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Data settings")
                    },
                    navigationIcon = {
                        BackButton {
                            navController?.popBackStack()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                SettingHeader("Image")
                ListSelector(
                    options = Quality.entries,
                    selectedOption = dataSettings.imageQuality.onWifi,
                    headlineContent = { Text("Preferred quality on wifi") },
                    onOptionSelected = { quality ->
                        scope.launch {
                            dataSettingsStore.updateData { settings ->
                                settings.copy(imageQuality = settings.imageQuality.copy(onWifi = quality))
                            }
                        }
                    }
                )
                ListSelector(
                    options = Quality.entries,
                    selectedOption = dataSettings.imageQuality.onMobile,
                    headlineContent = { Text("Preferred quality on cellular") },
                    onOptionSelected = { quality ->
                        scope.launch {
                            dataSettingsStore.updateData { settings ->
                                settings.copy(imageQuality = settings.imageQuality.copy(onMobile = quality))
                            }
                        }
                    }
                )
                ListSelector(
                    options = NetworkPolicy.entries,
                    selectedOption = dataSettings.imageQuality.loadImage,
                    headlineContent = { Text("Load image") },
                    onOptionSelected = { quality ->
                        scope.launch {
                            dataSettingsStore.updateData { settings ->
                                settings.copy(imageQuality = settings.imageQuality.copy(loadImage = quality))
                            }
                        }
                    }
                )
                SettingHeader("Video")
                ListSelector(
                    options = VideoQuality.entries,
                    selectedOption = dataSettings.videoQuality.onWifi,
                    headlineContent = { Text("Preferred quality on wifi") },
                    onOptionSelected = { quality ->
                        scope.launch {
                            dataSettingsStore.updateData { settings ->
                                settings.copy(videoQuality = settings.videoQuality.copy(onWifi = quality))
                            }
                        }
                    }
                )
                ListSelector(
                    options = VideoQuality.entries,
                    selectedOption = dataSettings.videoQuality.onMobile,
                    headlineContent = { Text("Preferred quality on cellular") },
                    onOptionSelected = { quality ->
                        scope.launch {
                            dataSettingsStore.updateData { settings ->
                                settings.copy(videoQuality = settings.videoQuality.copy(onMobile = quality))
                            }
                        }
                    }
                )
                ListSelector(
                    options = NetworkPolicy.entries,
                    selectedOption = dataSettings.videoQuality.autostart,
                    headlineContent = { Text("Autostart video") },
                    onOptionSelected = { quality ->
                        scope.launch {
                            dataSettingsStore.updateData { settings ->
                                settings.copy(videoQuality = settings.videoQuality.copy(autostart = quality))
                            }
                        }
                    }
                )
            }
        }
    }
}