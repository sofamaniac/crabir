package com.sofamaniac.crabir.settings.post

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.SettingHeader
import com.sofamaniac.crabir.settings.helper.SwitchTile
import kotlinx.coroutines.launch

@Composable
fun PostSettingsPage() {
    val context = LocalContext.current
    val postSettingsDataStore = remember(context) { context.postSettingsDataStore }
    val postSettings by postSettingsDataStore.data.collectAsState(
        initial = PostSettingsDefaults.defaultPostSettings,
    )
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    fun update(transform: (PostSettings) -> PostSettings) {
        scope.launch {
            postSettingsDataStore.updateData(transform)
        }
    }

    fun updateAwardsSettings(transform: (AwardSettings) -> AwardSettings) {
        update { it.copy(awardSettings = transform(it.awardSettings)) }
    }

    fun updateFlairSettings(transform: (FlairSettings) -> FlairSettings) {
        update { it.copy(flairSettings = transform(it.flairSettings)) }
    }

    fun updateInfoSettings(transform: (InfoSettings) -> InfoSettings) {
        update { it.copy(infoSettings = transform(it.infoSettings)) }
    }

    fun updateButtonsSettings(transform: (ButtonsSettings) -> ButtonsSettings) {
        update { it.copy(buttonsSettings = transform(it.buttonsSettings)) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Post Settings") }, navigationIcon = {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            //awardsSettings(postSettings, ::updateAwardsSettings)
            flairSettings(postSettings, ::updateFlairSettings)
        }
    }
}

fun LazyListScope.awardsSettings(
    postSettings: PostSettings,
    updateAwardsSettings: (transform: (AwardSettings) -> AwardSettings) -> Unit,
) {
    item {
        SettingHeader(stringResource(R.string.awards_settings_header))
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show awards") },
            checked = postSettings.awardSettings.showAwards,
            onCheckedChange = { target ->
                updateAwardsSettings { it.copy(showAwards = target) }
            }
        )
    }
    item {
        val theme = LocalTheme.current
        val enabled = postSettings.awardSettings.showAwards
        SwitchTile(
            headlineContent = {
                Text(
                    "Clickable awards",
                )
            },
            enabled = enabled,
            checked = postSettings.awardSettings.clickableAwards,
            onCheckedChange = { target ->
                updateAwardsSettings { it.copy(clickableAwards = target) }
            }
        )
    }
}

fun LazyListScope.flairSettings(
    postSettings: PostSettings,
    updateFlairSettings: (transform: (FlairSettings) -> FlairSettings) -> Unit,
) {
    item {
        SettingHeader("Flair settings")
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show flair") },
            checked = postSettings.flairSettings.showFlair,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(showFlair = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show flair color") },
            enabled = postSettings.flairSettings.showFlair,
            checked = postSettings.flairSettings.showFlairColor,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(showFlairColor = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show flair emoji") },
            enabled = postSettings.flairSettings.showFlair,
            checked = postSettings.flairSettings.showFlairEmoji,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(showFlairEmoji = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Click on flair to search") },
            enabled = postSettings.flairSettings.showFlair,
            checked = postSettings.flairSettings.clickable,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(clickable = target) }
            },
        )
    }
}