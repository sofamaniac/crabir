package com.sofamaniac.crabir.ui.editor

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.CloseButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TextEditor(
    name: Fullname,
    initial: String,
    viewModel: TextEditorViewModel = koinViewModel(key = name.name) {
        parametersOf(initial)
    },
) {
    val navController = LocalNavController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Error -> {
                snackbarHostState.showSnackbar((uiState as UiState.Error).message)
            }

            is UiState.Done -> {
                navController?.popBackStack()
            }

            else -> {}
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopBar(
                dismiss = { navController?.popBackStack() },
                submit = { viewModel.submit(name) },
                loading = uiState is UiState.Loading
            )
        },
        bottomBar = { EditorBottomBar(viewModel.state) }
    ) { paddingValues ->
        TextField(
            state = viewModel.state,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun TopBar(dismiss: () -> Unit, submit: () -> Unit, loading: Boolean) {
    TopAppBar(
        title = { Text(stringResource(R.string.edit)) },
        navigationIcon = {
            CloseButton(onClick = dismiss)
        },
        actions = {
            if (!loading) {
                IconButton(onClick = submit) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.submit)
                    )
                }
            } else {
                CircularProgressIndicator()
            }
        }
    )
}

@KoinViewModel
class TextEditorViewModel(
    private val api: RedditAPIService,
    @InjectedParam initial: String,
) : ViewModel() {
    val state = TextFieldState(initial)

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState

    fun submit(name: Fullname) {
        if (_uiState.value == UiState.Loading) return
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val text = state.text as String
            api.editUserText(name, text).onSuccess {
                _uiState.value = UiState.Done
            }.onFailure {
                _uiState.value = UiState.Error(it.message ?: "Unknown error")
            }
        }
    }
}

sealed class UiState {
    object Loading : UiState()
    object Initial : UiState()
    data class Error(val message: String) : UiState()
    object Done : UiState()
}
