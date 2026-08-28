package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.RandditAPI
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.SubredditRoute
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinViewModel

@Composable
internal fun RandomCommunity(label: String, includeNsfw: Boolean) {
    val navController = LocalNavController.current
    val viewModel: RandditViewModel = koinViewModel()
    NavigationDrawerItem(selected = false, label = { Text(label) }, onClick = {
        viewModel.getRandom(
            includeNsfw, {
                val subreddit = it.removePrefix("/")
                val route = SubredditRoute(subreddit)
                navController?.navigate(route)
            },
            {
                it.printStackTrace()
            }
        )
    })
}

@KoinViewModel
class RandditViewModel(private val api: RandditAPI) : ViewModel() {
    fun getRandom(
        includeNsfw: Boolean,
        onSuccess: (String) -> Unit,
        onError: (Throwable) -> Unit,
    ) {
        viewModelScope.launch {
            val response = api.getRandomCommunity(includeNsfw)
            if (response.isSuccess) {
                onSuccess(response.getOrNull()!!.url)
            } else {
                onError(response.exceptionOrNull()!!)
            }
        }
    }
}