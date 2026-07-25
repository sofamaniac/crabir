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

    fun updateLinksSettings(transform: (LinksSettings) -> LinksSettings) {
        update { it.copy(linksSettings = transform(it.linksSettings)) }
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
            flairSettings(postSettings.flairSettings, ::updateFlairSettings)
            infoSettings(postSettings.infoSettings, ::updateInfoSettings)
            linksSettings(postSettings.linksSettings, ::updateLinksSettings)
            buttonsSettings(postSettings.buttonsSettings, ::updateButtonsSettings)
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

fun LazyListScope.linksSettings(
    linksSettings: LinksSettings,
    updateLinksSettings: (transform: (LinksSettings) -> LinksSettings) -> Unit,
) {
    item {
        SettingHeader("Links settings")
    }
    item {
        SwitchTile(
            headlineContent = { Text("Upvote on save") },
            checked = linksSettings.upvoteOnSave,
            onCheckedChange = { target ->
                updateLinksSettings { it.copy(upvoteOnSave = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Start videos muted") },
            checked = linksSettings.startMuted,
            onCheckedChange = { target ->
                updateLinksSettings { it.copy(startMuted = target) }
            },
        )
    }
}

fun LazyListScope.infoSettings(
    infoSettings: InfoSettings,
    updateInfoSettings: (transform: (InfoSettings) -> InfoSettings) -> Unit,
) {
    item {
        SettingHeader("Info settings")
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show author") },
            checked = infoSettings.showAuthor,
            onCheckedChange = { target ->
                updateInfoSettings { it.copy(showAuthor = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Tap on author to go to profile") },
            enabled = infoSettings.showAuthor,
            checked = infoSettings.clickableAuthor,
            onCheckedChange = { target ->
                updateInfoSettings { it.copy(clickableAuthor = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show community") },
            checked = infoSettings.clickableCommunity,
            onCheckedChange = { target ->
                updateInfoSettings { it.copy(clickableCommunity = target) }
            },
        )
    }
}

fun LazyListScope.flairSettings(
    flairSettings: FlairSettings,
    updateFlairSettings: (transform: (FlairSettings) -> FlairSettings) -> Unit,
) {
    item {
        SettingHeader("Flair settings")
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show flair") },
            checked = flairSettings.showFlair,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(showFlair = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show flair color") },
            enabled = flairSettings.showFlair,
            checked = flairSettings.showFlairColor,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(showFlairColor = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show flair emoji") },
            enabled = flairSettings.showFlair,
            checked = flairSettings.showFlairEmoji,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(showFlairEmoji = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Click on flair to search") },
            enabled = flairSettings.showFlair,
            checked = flairSettings.clickable,
            onCheckedChange = { target ->
                updateFlairSettings { it.copy(clickable = target) }
            },
        )
    }
}

fun LazyListScope.buttonsSettings(
    buttonsSettings: ButtonsSettings,
    updateButtonsSettings: (transform: (ButtonsSettings) -> ButtonsSettings) -> Unit,
) {
    item {
        SettingHeader("Buttons settings")
    }
    item {
        SwitchTile(
            headlineContent = { Text("Show comments") },
            checked = buttonsSettings.comments,
            onCheckedChange = { target ->
                updateButtonsSettings { it.copy(comments = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Hide post") },
            checked = buttonsSettings.hide,
            onCheckedChange = { target ->
                updateButtonsSettings { it.copy(hide = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Share post") },
            checked = buttonsSettings.share,
            onCheckedChange = { target ->
                updateButtonsSettings { it.copy(share = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Open in app") },
            checked = buttonsSettings.openInApp,
            onCheckedChange = { target ->
                updateButtonsSettings { it.copy(openInApp = target) }
            },
        )
    }
    item {
        SwitchTile(
            headlineContent = { Text("Mark as read") },
            checked = buttonsSettings.markAsRead,
            onCheckedChange = { target ->
                updateButtonsSettings { it.copy(markAsRead = target) }
            },
        )
    }
}