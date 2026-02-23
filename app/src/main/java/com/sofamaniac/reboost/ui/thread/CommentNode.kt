package com.sofamaniac.reboost.ui.thread

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.sofamaniac.reboost.LocalNavController
import com.sofamaniac.reboost.ProfileRoute
import com.sofamaniac.reboost.domain.model.CommentData
import com.sofamaniac.reboost.settings.DefaultReboostTheme
import com.sofamaniac.reboost.settings.themeDataStore
import com.sofamaniac.reboost.ui.Cartouche
import com.sofamaniac.reboost.ui.Flair
import com.sofamaniac.reboost.ui.formatElapsedTimeLocalized
import com.sofamaniac.reboost.ui.markdown.SimpleMarkdown
import com.sofamaniac.reboost.ui.post.DownButton
import com.sofamaniac.reboost.ui.post.SavedButton
import com.sofamaniac.reboost.ui.post.UpButton
import kotlinx.coroutines.flow.map

@Composable
fun CommentNode(comment: CommentData, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {


    val context = LocalContext.current
    val showBottomBar by remember(comment.name, context) {
        viewModel.openComment.map { it == comment.name }
    }.collectAsState(initial = false)

    val innerModifier = Modifier
        .padding(horizontal = 8.dp)
        .padding(vertical = 8.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { viewModel.toggleComment(comment.name) }),
    ) {
        TopRow(comment, modifier = innerModifier)
        SimpleMarkdown(
            comment.bodyMd,
            modifier = innerModifier,
            mediaMetadata = comment.mediaMetadata,
            key = comment.id
        )
        AnimatedVisibility(showBottomBar) {
            BottomRow(comment, viewModel)
        }
    }
}

@Composable
fun BottomRow(comment: CommentData, viewModel: ThreadViewModel, modifier: Modifier = Modifier) {
    val likes = comment.relationship.liked
    val saved = comment.relationship.saved
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.2f)),
        horizontalArrangement = Arrangement.End
    ) {
        UpButton(likes) { viewModel.upvote(comment.name, likes) }
        DownButton(likes) { viewModel.downvote(comment.name, likes) }
        SavedButton(saved) { viewModel.save(comment.name, saved) }
    }
}

@Composable
fun TopRow(comment: CommentData, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val themeDataStore = remember(context) { context.themeDataStore }
    val theme by themeDataStore.data.collectAsState(
        initial = DefaultReboostTheme,
        coroutineScope.coroutineContext
    )

    val timeString = formatElapsedTimeLocalized(comment.createdUtc)
    val delta = when (comment.relationship.liked) {
        true -> 1
        false -> -1
        else -> 0
    }
    val rightString = buildAnnotatedString {
        append("${comment.score.ups + delta}")
        append(" · ")
        append(timeString)
    }

    val navController = LocalNavController.current!!
    val authorModifier = Modifier.clickable {
        navController.navigate(ProfileRoute(comment.author.username))
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (comment.isSubmitter) {
            Cartouche(backgroundColor = Color(0xFF2196F3)) {
                Text(comment.author.username, color = Color.White, modifier = authorModifier)
            }
        } else {
            Text(comment.author.username, color = theme.highlight, modifier = authorModifier)
        }
        Flair(comment.author.flair)
        Spacer(modifier = Modifier.weight(1f))
        Text(rightString)
    }
}
