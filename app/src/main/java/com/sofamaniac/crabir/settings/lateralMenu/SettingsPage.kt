package com.sofamaniac.crabir.settings.lateralMenu

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.settings.helper.CheckboxTile
import com.sofamaniac.crabir.settings.helper.SettingHeader
import com.sofamaniac.crabir.settings.helper.SwitchTile
import com.sofamaniac.crabir.ui.BackButton
import kotlinx.coroutines.launch

@Composable
fun LateralMenuSettingsPage() {
    val context = LocalContext.current
    val datastore = remember(context) { context.lateralMenuSettingsDataStore }
    val lateralMenuSettings by datastore.data.collectAsState(
        initial = LateralMenuSettingsDefault.default,
    )
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    fun update(transform: (LateralMenuSettings) -> LateralMenuSettings) {
        scope.launch {
            datastore.updateData(transform)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lateral Menu Settings") }, navigationIcon = {
                BackButton {
                    navController?.popBackStack()
                }
            })
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                SettingHeader(stringResource(R.string.lateral_menu_items_header))
            }
            itemsToShow(lateralMenuSettings.items) { target ->
                update { it.copy(items = target) }
            }
            item {
                SettingHeader(stringResource(R.string.subscriptions_list))
            }
            item {
                SwitchTile(
                    headlineContent = { Text("Show in menu") },
                    checked = lateralMenuSettings.showSubscriptions,
                    onCheckedChange = { target ->
                        update { it.copy(showSubscriptions = target) }
                    }
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text("Show icons") },
                    enabled = lateralMenuSettings.showSubscriptions,
                    checked = lateralMenuSettings.showIcons,
                    onCheckedChange = { target ->
                        update { it.copy(showIcons = target) }
                    }
                )
            }
            item {
                SwitchTile(
                    headlineContent = { Text("Show only favorites") },
                    enabled = lateralMenuSettings.showSubscriptions,
                    checked = lateralMenuSettings.showFavOnly,
                    onCheckedChange = { target ->
                        update { it.copy(showFavOnly = target) }
                    }
                )
            }
        }
    }
}

private fun LazyListScope.itemsToShow(
    settings: LateralMenuItems,
    update: (LateralMenuItems) -> Unit,
) {

    item {
        CheckboxTile(
            headlineContent = { Text("Home Feed") },
            supportingContent = { Text("Posts from subscriptions") },
            leadingContent = { Icon(Icons.Default.Home, contentDescription = null) },
            checked = settings.homeFeed,
            onCheckedChange = { target ->
                update(settings.copy(homeFeed = target))
            }
        )
    }

    item {
        CheckboxTile(
            headlineContent = { Text("Popular") },
            leadingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null
                )
            },
            checked = settings.popular,
            onCheckedChange = { target ->
                update(settings.copy(popular = target))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("All") },
            leadingContent = {
                Icon(
                    Icons.Default.BarChart,
                    contentDescription = null
                )
            },
            checked = settings.all,
            onCheckedChange = { taget ->
                update(settings.copy(all = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Saved") },
            leadingContent = {
                Icon(
                    Icons.Default.BookmarkBorder,
                    contentDescription = null
                )
            },
            checked = settings.saved,
            onCheckedChange = { taget ->
                update(settings.copy(saved = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("History") },
            leadingContent = {
                Icon(
                    Icons.Default.History,
                    contentDescription = null
                )
            },
            checked = settings.history,
            onCheckedChange = { taget ->
                update(settings.copy(history = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Profile") },
            leadingContent = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )
            },
            checked = settings.profile,
            onCheckedChange = { taget ->
                update(settings.copy(profile = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Inbox") },
            leadingContent = {
                Icon(
                    Icons.Default.Inbox,
                    contentDescription = null
                )
            },
            checked = settings.inbox,
            onCheckedChange = { taget ->
                update(settings.copy(inbox = taget))
            }
        )
    }
    item {
        CheckboxTile(
            enabled = false,
            headlineContent = { Text("Friends") },
            leadingContent = {
                Icon(
                    Icons.Default.Group,
                    contentDescription = null
                )
            },
            checked = settings.friends,
            onCheckedChange = { taget ->
                update(settings.copy(friends = taget))
            }
        )
    }
    item {
        CheckboxTile(
            enabled = false,
            headlineContent = { Text("Drafts") },
            leadingContent = {
                Icon(
                    Icons.Default.Drafts,
                    contentDescription = null
                )
            },
            checked = settings.drafts,
            onCheckedChange = { taget ->
                update(settings.copy(drafts = taget))
            }
        )
    }
    item {
        CheckboxTile(
            enabled = false,
            headlineContent = { Text("Moderation") },
            leadingContent = {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = null
                )
            },
            checked = settings.moderation,
            onCheckedChange = { taget ->
                update(settings.copy(moderation = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Search") },
            leadingContent = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null
                )
            },
            checked = settings.search,
            onCheckedChange = { taget ->
                update(settings.copy(search = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Go to menu") },
            leadingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null
                )
            },
            checked = settings.goToMenu,
            onCheckedChange = { taget ->
                update(settings.copy(goToMenu = taget))
            }
        )
    }

    item {
        CheckboxTile(
            headlineContent = { Text("Go to community") },
            leadingContent = {
                Icon(
                    Icons.Default.GroupWork,
                    contentDescription = null
                )
            },
            checked = settings.goToCommunity,
            onCheckedChange = { taget ->
                update(settings.copy(goToCommunity = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Go to user") },
            leadingContent = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )
            },
            checked = settings.goToUser,
            onCheckedChange = { taget ->
                update(settings.copy(goToUser = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Dark mode") },
            leadingContent = {
                Icon(
                    Icons.Default.DarkMode,
                    contentDescription = null
                )
            },
            checked = settings.darkMode,
            onCheckedChange = { taget ->
                update(settings.copy(darkMode = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Blur NSFW") },
            leadingContent = {
                Icon(
                    Icons.Default.BlurOn,
                    contentDescription = null
                )
            },
            checked = settings.blurNSFW,
            onCheckedChange = { taget ->
                update(settings.copy(blurNSFW = taget))
            }
        )
    }
    item {
        CheckboxTile(
            headlineContent = { Text("Show NSFW") },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.eighteen_rating),
                    contentDescription = null
                )
            },
            checked = settings.showNSFW,
            onCheckedChange = { taget ->
                update(settings.copy(showNSFW = taget))
            }
        )
    }
}