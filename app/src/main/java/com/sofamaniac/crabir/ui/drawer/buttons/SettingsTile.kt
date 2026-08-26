package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SettingsRoute
import com.sofamaniac.crabir.settings.theme.ThemeMode
import com.sofamaniac.crabir.settings.theme.themeDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
internal fun SettingsTile(drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val context = LocalContext.current
    val themeDataStore = remember { context.themeDataStore }
    val themeMode by remember {
        themeDataStore.data.map { it.mode }
    }
        .collectAsState(initial = ThemeMode.System)
    NavigationDrawerItem(
        label = {
            Text(stringResource(R.string.settings))
        },
        badge = {
            if (themeMode == ThemeMode.Dark || themeMode == ThemeMode.Light) {
                IconButton(onClick = {
                    coroutineScope.launch {
                        themeDataStore.updateData {
                            if (themeMode == ThemeMode.Dark) {
                                it.copy(mode = ThemeMode.Light)
                            } else {
                                it.copy(mode = ThemeMode.Dark)
                            }
                        }
                    }
                }) {
                    if (themeMode == ThemeMode.Dark) {
                        Icon(Icons.Default.LightMode, contentDescription = "Light mode")
                    } else {
                        Icon(Icons.Default.DarkMode, contentDescription = "Dark mode")
                    }
                }
            }
        },
        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
        selected = false,
        onClick = {
            coroutineScope.launch {
                drawerState.close()
                navController?.navigate(SettingsRoute)
            }
        }
    )
}