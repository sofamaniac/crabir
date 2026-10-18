package com.sofamaniac.crabir.ui.thread.comment

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sofamaniac.crabir.BuildConfig
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.votable.CommentData
import com.sofamaniac.crabir.ui.thread.ReplyButton
import com.sofamaniac.crabir.ui.thread.dialog.MoreOptionButton
import com.sofamaniac.crabir.ui.votable.DownButton
import com.sofamaniac.crabir.ui.votable.SavedButton
import com.sofamaniac.crabir.ui.votable.UpButton

@Composable
fun BottomRow(
    comment: CommentData,
    viewModel: CommentViewModel,
    startReply: () -> Unit,
    modifier: Modifier = Modifier,
    onAction: () -> Unit,
) {
    val likes = comment.relationship.liked
    val saved = comment.relationship.saved
    CompositionLocalProvider(LocalContentColor provides LocalTheme.current.secondaryText) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(color = Color.Gray.copy(alpha = 0.2f)),
            horizontalArrangement = Arrangement.End
        ) {
            UpButton(likes, onClick = {
                viewModel.upvote(comment.name)
                onAction()
            })
            DownButton(likes, onClick = {
                viewModel.downvote(comment.name)
                onAction()
            })
            SavedButton(saved, onClick = {
                viewModel.save(comment.name, !saved, false)
                onAction()
            })
            ReplyButton(
                enabled = comment.canComment,
                startReply = startReply
            )
            MoreOptionButton(comment, viewModel)
            if (BuildConfig.DEBUG) {
                IconButton(onClick = {
                    Log.d("CommentNode", "$comment")
                }) {
                    Icon(Icons.Default.BugReport, contentDescription = null)
                }
            }
        }
    }
}
