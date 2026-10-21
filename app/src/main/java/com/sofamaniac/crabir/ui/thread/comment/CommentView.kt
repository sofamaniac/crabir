package com.sofamaniac.crabir.ui.thread.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.votable.CommentData
import com.sofamaniac.crabir.ui.components.ThemedCard
import com.sofamaniac.crabir.ui.components.cartouche
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.thread.depthIndent
import com.sofamaniac.crabir.ui.votable.CommentEvent
import com.sofamaniac.crabir.ui.votable.ScoreString

@Composable
fun CommentView(
    comment: CommentData,
    depth: Int,
    children: Int,
    opened: Boolean,
    highlight: Boolean,
    onEvent: (CommentEvent) -> Unit,
    modifier: Modifier = Modifier,
    enableAnimation: Boolean = true,
) {
    val innerModifier = Modifier
        .padding(horizontal = 16.dp)
    val theme = LocalTheme.current

    Column(modifier = modifier) {
        if (depth == 0) HorizontalDivider()
        ThemedCard(
            roundedCorners = false,
            highlight = highlight,
            modifier = Modifier
                .fillMaxWidth()
                .background(theme.cardBackground)
                .depthIndent(depth.coerceAtLeast(0))
                .combinedClickable(
                    onClick = { if (!comment.collapsed) onEvent(CommentEvent.ShowBar(comment.name)) },
                    onLongClick = { onEvent(CommentEvent.Collapse(comment.name)) },
                    onLongClickLabel = "Collapse comment"
                ),
        ) {
            if (comment.collapsed) {
                CollapsedComment(
                    comment,
                    children,
                    modifier = innerModifier.padding(vertical = 8.dp)
                )
            } else {
                OpenedComment(
                    comment,
                    opened = opened,
                    modifier = innerModifier,
                    enableAnimation = enableAnimation,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun CollapsedComment(
    comment: CommentData,
    children: Int,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val timeString = formatElapsedTimeLocalized(comment.createdUtc)
    val rightString = buildAnnotatedString {
        append("· ")
        append(timeString)
    }
    val authorString = buildAnnotatedString {
        append("[+] ")
        if (comment.distinguished == "moderator") {
            withStyle(SpanStyle(background = Color(0xFFB2FF59))) {
                append(comment.author.username)
            }
        } else {
            append(comment.author.username)
        }
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(authorString, color = theme.secondaryText)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            "+$children",
            color = Color.White,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.cartouche(backgroundColor = Color.Green)
        )
        ScoreString(
            comment.score.score,
            comment.relationship.liked,
            hidden = comment.score.hideScore
        )
        Text(rightString)
    }
}
