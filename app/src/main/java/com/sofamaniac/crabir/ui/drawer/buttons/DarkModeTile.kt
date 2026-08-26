package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.Icon
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
import com.sofamaniac.crabir.settings.theme.ThemeMode
import com.sofamaniac.crabir.settings.theme.themeDataStore
import com.sofamaniac.crabir.ui.ThemedSwitch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun DarkModeTile() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val themeDataStore = remember { context.themeDataStore }
    val themeMode by remember {
        themeDataStore.data.map { it.mode }
    }
        .collectAsState(initial = ThemeMode.System)

    fun onClick() {
        coroutineScope.launch {
            themeDataStore.updateData {
                if (themeMode == ThemeMode.Dark) {
                    it.copy(mode = ThemeMode.Light)
                } else {
                    it.copy(mode = ThemeMode.Dark)
                }
            }
        }
    }
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.DarkMode, contentDescription = null) },
        label = { Text(stringResource(R.string.DarkMode)) },
        badge = {
            ThemedSwitch(checked = themeMode == ThemeMode.Dark, onCheckedChange = {
                onClick()
            })
        },
        selected = false,
        onClick = {
            onClick()
        }
    )
}