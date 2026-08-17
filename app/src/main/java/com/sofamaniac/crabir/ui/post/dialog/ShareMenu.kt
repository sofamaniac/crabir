package com.sofamaniac.crabir.ui.post.dialog

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.CrosspostCreatorRoute
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.ui.ThemedDialog

@Composable
fun ShareMenu(post: PostData, onDismissRequest: () -> Unit) {
    val permalink = "https://reddit.com${post.permalink}"
    val shareLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onDismissRequest()
        }
    val navController = LocalNavController.current!!
    ThemedDialog(onDismissRequest) {
        ListItem(
            leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
            content = { Text(stringResource(R.string.share_link)) },
            supportingContent = {
                Text(
                    post.url,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, post.url)
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
            content = { Text(stringResource(R.string.share_post)) },
            supportingContent = {
                Text(
                    permalink,
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
        val titleLink = "${post.title} $permalink"
        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.TextFields,
                    contentDescription = null
                )
            },
            content = { Text(stringResource(R.string.share_title_link)) },
            supportingContent = {
                Text(
                    titleLink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, titleLink)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, null)
                shareLauncher.launch(shareIntent)
            }
        )
        HorizontalDivider()
        ListItem(
            leadingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.CallSplit,
                    modifier = Modifier.rotate(90f),
                    contentDescription = null,
                )
            },
            content = { Text(stringResource(R.string.crosspost)) },
            modifier = Modifier.clickable {
                onDismissRequest()
                navController.navigate(CrosspostCreatorRoute(post.name))
            }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Link, contentDescription = null) },
            content = { Text(stringResource(R.string.share_shortlink)) },
            supportingContent = {
                Text(
                    post.shortlink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, post.shortlink)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, null)
                shareLauncher.launch(shareIntent)
            }
        )
    }
}