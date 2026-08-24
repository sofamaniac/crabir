package com.sofamaniac.crabir.settings.comments

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import com.sofamaniac.crabir.data.remote.dto.comment.Sort
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.ListSelector
import com.sofamaniac.crabir.settings.helper.SettingHeader
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.settings.views.ImageHeight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CommentsSettingsPage() {
    val context = LocalContext.current
    val settingsDataStore = remember(context) { context.commentsSettingsDataStore }
    val commentsSettingsOpt by settingsDataStore.data.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.comments_settings_page_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                })
        }
    ) { innerPadding ->
        if (commentsSettingsOpt == null) return@Scaffold
        val commentsSettings = commentsSettingsOpt!!
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                SettingHeader(stringResource(R.string.header_sort))
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.use_recommended_sort)) },
                    checked = commentsSettings.useRecommendedSort,
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(useRecommendedSort = target)
                            }
                        }
                    }
                )
            }
            item {
                ListSelector(
                    enabled = !commentsSettings.useRecommendedSort,
                    options = Sort.entries.toList(),
                    headlineContent = { Text(stringResource(R.string.preferred_sort)) },
                    selectedOption = commentsSettings.preferredSort,
                    onOptionSelected = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(preferredSort = target)
                            }
                        }
                    }
                )
            }
            item {
                SettingHeader(stringResource(R.string.appearance))
            }

            item {
                ListSelector(
                    headlineContent = { Text(stringResource(R.string.media_preview)) },
                    options = ImageHeight.entries.toList(),
                    selectedOption = commentsSettings.postMediaPreview,
                    onOptionSelected = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(postMediaPreview = target)
                            }
                        }
                    }
                )
            }

            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.buttons_always_visible)) },
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(buttonsAlwaysVisible = target)
                            }
                        }
                    },
                    checked = commentsSettings.buttonsAlwaysVisible
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.hide_buttons_after_vote)) },
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(hideButtonsAfterVote = target)
                            }
                        }
                    },
                    checked = commentsSettings.hideButtonsAfterVote
                )
            }

            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.collapse_automod)) },
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(collapseAutoMod = target)
                            }
                        }
                    },
                    checked = commentsSettings.collapseAutoMod
                )
            }

            item {
                SettingHeader(stringResource(R.string.navigation_header))
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.show_navigation_bar)) },
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(showNavigationBar = target)
                            }
                        }
                    },
                    checked = commentsSettings.showNavigationBar
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text(stringResource(R.string.use_volume_keys_to_navigate)) },
                    onCheckedChange = { target ->
                        scope.launch {
                            settingsDataStore.updateData {
                                it.copy(useVolumeKeyNavigation = target)
                            }
                        }
                    },
                    checked = commentsSettings.useVolumeKeyNavigation
                )
            }
        }
    }
}
