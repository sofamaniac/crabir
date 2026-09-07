package com.sofamaniac.crabir.ui.thread.dialog

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.CommentData
import com.sofamaniac.crabir.ui.ListItem
import com.sofamaniac.crabir.ui.ThemedCard

@Composable
fun ShareMenu(comment: CommentData, onDismissRequest: () -> Unit) {
    val permalink = "https://reddit.com${comment.permalink}"
    val shareLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onDismissRequest()
        }
    Dialog(onDismissRequest) {
        ThemedCard(modifier = Modifier.padding(16.dp)) {
            ListItem(
                leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
                content = { Text(stringResource(R.string.share_link)) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, permalink)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    shareLauncher.launch(shareIntent)
                }
            )
            ListItem(
                leadingContent = {
                    Icon(
                        Icons.AutoMirrored.Default.Comment,
                        contentDescription = null
                    )
                },
                content = { Text(stringResource(R.string.share_comment_text)) },
                supportingContent = {
                    Text(
                        comment.body.markdown,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, permalink)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    shareLauncher.launch(shareIntent)
                }
            )
        }
    }
}

