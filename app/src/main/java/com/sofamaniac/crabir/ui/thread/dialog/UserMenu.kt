package com.sofamaniac.crabir.ui.thread.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.ProfileRoute
import com.sofamaniac.crabir.settings.filters.filtersDataStore
import com.sofamaniac.crabir.ui.components.ListItem
import com.sofamaniac.crabir.ui.components.ThemedDialog
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinViewModel

@Composable
fun UserMenu(
    username: String,
    viewModel: UserMenuViewModel = koinViewModel(),
    onDismissRequest: () -> Unit,
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val filtersSettingsStore = context.filtersDataStore
    val scope = rememberCoroutineScope()
    ThemedDialog(onDismissRequest) {
        ListItem(
            onClick = {
                val route = ProfileRoute(username = username)
                navController?.navigate(route)
            },
            leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
            content = { Text("Go to $username's profile") },
        )
        ListItem(
            onClick = {
                viewModel.block(username)
            },
            leadingContent = {
                Icon(Icons.Default.Block, contentDescription = null)
            }, content = {
                Text("Block $username")
            }
        )
        ListItem(
            onClick = {
                scope.launch {
                    filtersSettingsStore.updateData {
                        it.addAuthor(username)
                    }
                }
            },
            leadingContent = {
                Icon(Icons.AutoMirrored.Filled.VolumeMute, contentDescription = null)
            }, content = {
                Text("Mute $username")
            }
        )
    }
}

@KoinViewModel
class UserMenuViewModel(private val api: RedditAPIService) : ViewModel() {
    fun block(username: String) {
        viewModelScope.launch {
            api.block(username)
        }
    }
}
