package com.sofamaniac.crabir.ui.inbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.AccountManager
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.data.remote.reddit.InboxAPI
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.InboxRepository
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.CloseButton
import com.sofamaniac.crabir.ui.editor.AccountSelector
import com.sofamaniac.crabir.ui.editor.EditorBottomBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MessageEditor(
    parent: Fullname? = null,
    viewModel: MessageEditorViewModel = koinViewModel() {
        parametersOf(parent)
    },
) {
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val initialAccount = LocalRedditAccount.current
    var selectedAccount by remember { mutableStateOf(initialAccount) }
    val accounts by viewModel.accounts.collectAsState(emptyList())
    val destState = rememberTextFieldState()
    val bodyState = rememberTextFieldState()
    val subjectState = rememberTextFieldState()
    val message by viewModel.message.collectAsState(null)
    val errors by viewModel.errors.collectAsState()
    LaunchedEffect(message) {
        if (message != null) {
            destState.edit {
                delete(0, destState.selection.start)
                insert(0, message!!.author ?: "")
            }
            subjectState.edit {
                delete(0, subjectState.selection.start)
                insert(0, message!!.subject ?: "")
            }
        }
    }
    val navController = LocalNavController.current

    Scaffold(
        bottomBar = { EditorBottomBar(bodyState) },
        topBar = {
            TopAppBar(title = { Text("Send Message") }, navigationIcon = {
                CloseButton {
                    navController?.popBackStack()
                }
            }, actions = {
                IconButton(onClick = {
                    if (parent == null) {
                        viewModel.sendMessage(
                            destState.text.toString(),
                            bodyState.text.toString(),
                            subjectState.text.toString(),
                            account = selectedAccount,
                            onError =
                                { e ->
                                    scope.launch {
                                        snackbar.showSnackbar(e.message!!)
                                    }
                                },
                            onSuccess = { navController?.popBackStack() }
                        )
                    } else {
                        viewModel.sendReply(
                            parent,
                            bodyState.text.toString(),
                            account = selectedAccount,
                            onError =
                                { e ->
                                    scope.launch {
                                        snackbar.showSnackbar(e.message!!)
                                    }
                                },
                            onSuccess = { navController?.popBackStack() }
                        )
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                }
            })
        },
        snackbarHost = { SnackbarHost(snackbar) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(state = rememberScrollState())
        ) {
            ListItem(leadingContent = { Text("From") }) {
                AccountSelector(accounts, selectedAccount) { newId ->
                    selectedAccount = accounts.find { it.id == newId }!!
                }
            }
            HorizontalDivider()
            ListItem(leadingContent = { Text("To") }) {
                TextField(
                    readOnly = parent != null,
                    isError = errors.contains(MessageEditorError.MissingDestination),
                    state = destState,
                    label = { Text("Destination") }
                )
            }
            ListItem(leadingContent = { Text("Subject") }) {
                TextField(
                    readOnly = parent != null,
                    isError = errors.contains(MessageEditorError.MissingSubject),
                    state = subjectState,
                    label = { Text("Subject") }
                )
            }
            TextField(
                isError = errors.contains(MessageEditorError.MissingBody),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .heightIn(min = 100.dp),
                state = bodyState,
                placeholder = { Text("Type content") },
                label = { Text("Content") }
            )
        }
    }
}

enum class MessageEditorError {
    MissingDestination,
    MissingBody,
    MissingSubject
}

@KoinViewModel
class MessageEditorViewModel(
    val accountManager: AccountManager,
    val messageRepository: InboxRepository,
    val api: InboxAPI,
    @InjectedParam val parent: Fullname?,
) : ViewModel() {
    val accounts = accountManager.accountsRepository.accounts

    val message = parent?.let {
        messageRepository.get(it)
    } ?: flowOf(null)

    private var errorsFlow = MutableStateFlow<Set<MessageEditorError>>(emptySet())
    val errors: StateFlow<Set<MessageEditorError>> = errorsFlow

    fun sendMessage(
        dest: String,
        body: String,
        subject: String,
        account: RedditAccount,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit,
    ) {
        errorsFlow.value = emptySet()
        if (account.isAnonymous()) {
            onError(Exception("Anonymous accounts cannot send messages"))
            return
        }
        if (dest.isBlank()) {
            onError(Exception("\"To\" cannot be empty"))
            errorsFlow.update { it + MessageEditorError.MissingDestination }
        }
        if (body.isBlank()) {
            onError(Exception("Body cannot be empty"))
            errorsFlow.update { it + MessageEditorError.MissingBody }
        }
        if (subject.isBlank()) {
            onError(Exception("Subject cannot be empty"))
            errorsFlow.update { it + MessageEditorError.MissingSubject }
        }
        if (errorsFlow.value.isNotEmpty()) {
            return
        }
        viewModelScope.launch {
            try {
                val res = api.compose(dest, subject, body, account = account)
                if (res.isSuccessful) {
                    val errs = res.body()?.json?.errors
                    if (!errs.isNullOrEmpty()) {
                        throw Exception(errs.toString())
                    }
                } else {
                    throw Exception(res.errorBody()?.string())
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun sendReply(
        parent: Fullname,
        body: String,
        account: RedditAccount,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit,
    ) {
        if (account.isAnonymous()) {
            onError(Exception("Anonymous accounts cannot send messages"))
            return
        }
        errorsFlow.value = emptySet()
        if (body.isBlank()) {
            onError(Exception("Body cannot be empty"))
            errorsFlow.update { it + MessageEditorError.MissingBody }
        }
        if (errorsFlow.value.isNotEmpty()) {
            return
        }
        viewModelScope.launch {
            try {
                val res = api.reply(body, parent, account = account)
                if (res.isSuccessful) {
                    val errs = res.body()?.json?.errors
                    if (!errs.isNullOrEmpty()) {
                        throw Exception(errs.toString())
                    }
                } else {
                    throw Exception(res.errorBody()?.string())
                }
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
