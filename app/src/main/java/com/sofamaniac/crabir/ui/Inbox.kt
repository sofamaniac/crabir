package com.sofamaniac.crabir.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.dto.Thing
import com.sofamaniac.crabir.data.remote.dto.emptyListing
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxView(viewModel: InboxViewModel = hiltViewModel()) {
    val messages by viewModel.messages
    Scaffold(topBar = { TopAppBar(title = { Text("Inbox") }) }) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(messages.size, key = { index -> messages[index].name }) { index ->
                val message = messages[index]
                Column {
                    when (message) {
                        is Thing.Message -> {
                            Message(message, modifier = Modifier.padding(8.dp))
                        }

                        is Thing.Comment -> {
                            Message(message, modifier = Modifier.padding(8.dp))
                        }

                        else -> {}
                    }

                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun Message(message: Thing.Message, modifier: Modifier = Modifier) {
    val message = message.data
    val header = @Composable {
        Row {
            Icon(Icons.Default.Mail, contentDescription = null)
            Text(message.subject)
        }
    }
    Message(
        header = header,
        author = message.author ?: "",
        body = message.body,
        created = message.createdUtc,
        modifier = modifier
    )
}

@Composable
fun Message(message: Thing.Comment, modifier: Modifier = Modifier) {
    val comment = message.data
    val icon = if (comment.type == "post_reply") {
        Icons.AutoMirrored.Filled.Message
    } else {
        Icons.AutoMirrored.Filled.Reply
    }
    val header = @Composable {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null)
            Text(comment.subject ?: "")
        }
    }
    Message(
        header = header,
        author = comment.author,
        subreddit = comment.subreddit,
        body = comment.body,
        created = comment.created_utc,
        modifier = modifier
    )
}

@Composable
fun Message(
    header: @Composable () -> Unit,
    author: String,
    created: Instant,
    body: String,
    modifier: Modifier = Modifier,
    subreddit: String? = null,
) {
    val theme = LocalTheme.current
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            header()
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }
        }
        val textStyle = MaterialTheme.typography.titleSmall
        val annotatedString = buildAnnotatedString {
            withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                append(author)
            }
            if (subreddit != null) {
                append(" via ")
                withStyle(textStyle.copy(color = theme.highlight).toSpanStyle()) {
                    append(subreddit)
                }
            }
            append(" · ")
            append(formatElapsedTimeLocalized(created))
        }
        Text(annotatedString)
        Spacer(modifier = Modifier.height(8.dp))
        RedditMarkdown(body, modifier = Modifier.padding(horizontal = 8.dp))
    }
}

@HiltViewModel
class InboxViewModel @Inject constructor(inbox: RedditAPIService) : ViewModel() {
    var _messages: MutableState<Thing.Listing<Thing>> = mutableStateOf(emptyListing())
    val messages: State<Thing.Listing<Thing>> = _messages

    init {
        viewModelScope.launch {
            val res = inbox.inbox()
            if (res.isSuccessful) {
                _messages.value = res.body()!!
            }
        }
    }
}