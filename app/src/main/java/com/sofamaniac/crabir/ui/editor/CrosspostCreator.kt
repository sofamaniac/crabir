package com.sofamaniac.crabir.ui.editor

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.remote.reddit.CrosspostSubmissionBuilder
import com.sofamaniac.crabir.data.remote.reddit.MissingTitle
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.SubmissionBuilderError
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.ThemedCard
import com.sofamaniac.crabir.ui.editor.postEditor.CommunitySelector
import com.sofamaniac.crabir.ui.editor.postEditor.CreatorViewModel
import com.sofamaniac.crabir.ui.post.PostHeader
import com.sofamaniac.crabir.ui.post.PostInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrosspostCreator(
    post: Fullname,
    viewModel: CrosspostCreatorViewModel = koinViewModel { parametersOf(post) }
) {
    val navController = LocalNavController.current
    val theme = LocalTheme.current
    val post = viewModel.post.collectAsState(initial = null).value ?: return
    LaunchedEffect(post) {
        viewModel.titleState.edit {
            delete(0, length)
            insert(0, post.title)
        }
        viewModel.state = viewModel.state.copy(
            title = post.title,
            nsfw = post.over18,
            spoiler = post.spoiler,
        )
    }
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val initialAccount = LocalRedditAccount.current
    var selectedAccount by remember { mutableStateOf(initialAccount) }
    val accounts by viewModel.accounts.collectAsState(emptyList())
    Box {
        Scaffold(
            modifier = Modifier.background(theme.cardBackground),
            snackbarHost = { SnackbarHost(snackbar) },
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController?.popBackStack() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    },
                    title = { Text("Create post") },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                val res = viewModel.submit(account = null)
                                if (res.isSuccess) {
                                    navController?.popBackStack()
                                } else {
                                    snackbar.showSnackbar(res.exceptionOrNull()!!.message!!)
                                }
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                        }
                    }
                )
            }) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(color = theme.cardBackground)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                CommunitySelector(viewModel)
                TextField(
                    state = viewModel.titleState,
                    label = { Text("Title") },
                    inputTransformation = InputTransformation.maxLength(300),
                    modifier = Modifier.fillMaxWidth(),
                    isError = viewModel.error is MissingTitle,
                    supportingText = {
                        if (viewModel.error is MissingTitle)
                            Text("Missing title", color = MaterialTheme.colorScheme.error)
                    },
                    trailingIcon = {
                        if (viewModel.error is MissingTitle)
                            Icon(
                                Icons.Filled.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                    }
                )
                Text(
                    "${viewModel.titleState.text.length}/300",
                    modifier = Modifier.align(Alignment.End)
                )
                if (viewModel.flairs.isNotEmpty()) {
                    TextButton(onClick = {}) {
                        Text("Flair")
                    }
                }
                AccountSelector(accounts, selectedAccount) { newId ->
                    selectedAccount = accounts.find { it.id == newId }!!
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        onClick = {
                            viewModel.state = viewModel.state.copy(nsfw = !viewModel.state.nsfw)
                        },
                        selected = viewModel.state.nsfw,
                        colors = FilterChipDefaults.filterChipColors().copy(
                            selectedContainerColor = Color.Red,
                        ),
                        label = {
                            Text("NSFW")
                        },
                    )
                    FilterChip(
                        onClick = {
                            viewModel.state =
                                viewModel.state.copy(spoiler = !viewModel.state.spoiler)
                        },
                        selected = viewModel.state.spoiler,
                        label = {
                            Text("SPOILER")
                        }
                    )
                }
                CrosspostView(post)
            }
        }
        if (viewModel.loading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
internal fun CrosspostView(
    post: PostData,
    modifier: Modifier = Modifier,
) {
    val modifier = modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)

    ThemedCard(
        roundedCorners = false,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PostHeader(
            post,
            modifier = modifier.padding(vertical = 8.dp)
        )
        val enablePreview = post.kind == Kind.Link || post.kind == Kind.Unknown
        PostInfo(
            post,
            modifier = modifier,
            enableThumbnail = enablePreview && !post.isCrosspost,
            likes = post.relationship.liked
        )
        Column {
            PostHeader(post, showSubredditIcon = false)
            PostInfo(
                post,
                modifier = modifier,
                enableThumbnail = true,
                likes = post.relationship.liked,
            )
        }
    }
}

@KoinViewModel
class CrosspostCreatorViewModel(
    @InjectedParam val parentFullname: Fullname,
    api: RedditAPIService,
    communities: SubredditRepository,
    linksRepository: LinksRepository,
    private val accountsRepository: AccountsRepository,
) : CreatorViewModel(api, communities) {
    var state by mutableStateOf(
        CrosspostSubmissionBuilder(
            crosspostFullname = parentFullname
        )
    )
    val post = linksRepository.get(parentFullname)

    var loading by mutableStateOf(false)
    val accounts: Flow<List<RedditAccount>> = accountsRepository.accounts

    suspend fun submit(account: RedditAccount?): Result<Unit> {
        loading = true
        state = state.copy(
            title = titleState.text as String,
            subreddit = community?.displayName ?: ""
        )
        val submission = state.build()
        if (submission.isFailure) {
            Log.e("PostCreatorViewModel", "submit: ${submission.exceptionOrNull()}")
            error = submission.exceptionOrNull() as SubmissionBuilderError?
            loading = false
            return Result.failure(error!!)
        } else {
            val res = api.submitPost(submission.getOrThrow(), account = account)
            loading = false
            return if (res.isSuccessful) {
                val response = res.body()
                Log.d("PostCreatorViewModel", "submit: $response")
                if (response?.json?.errors?.isNotEmpty() == true) {
                    Result.failure(Exception(response.json.errors.toString()))
                } else {
                    Result.success(Unit)
                }
            } else {
                Result.failure(Exception("Failed to submit post: ${res.errorBody()}"))
            }
        }
    }
}