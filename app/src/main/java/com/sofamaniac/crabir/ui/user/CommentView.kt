package com.sofamaniac.crabir.ui.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.domain.model.CommentType
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.ui.components.ThemedCard
import com.sofamaniac.crabir.ui.thread.CommentInner

@Composable
fun CommentView(
    thing: CommentType.Comment,
) {
    val navController = LocalNavController.current
    val comment = thing.comment
    val theme = LocalTheme.current
    ThemedCard(
        roundedCorners = false,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.clickable {
            navController?.navigate(
                PostRoute(
                    comment.permalink,
                    comment = thing.id,
                )
            )
        }
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            if (!comment.parentInfo.title.isNullOrBlank()) {
                Text(
                    comment.parentInfo.title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            if (comment.parentInfo.subreddit != null) {
                val text = buildAnnotatedString {
                    withStyle(MaterialTheme.typography.titleMedium.toSpanStyle()) {
                        append("in ")
                    }
                    withStyle(
                        MaterialTheme.typography.titleMedium.copy(color = theme.highlight)
                            .toSpanStyle()
                    ) {
                        append(comment.parentInfo.subreddit)
                    }
                }
                Text(
                    text,
                    style = MaterialTheme.typography.titleMedium,
                    color = theme.highlight
                )
            }
            CommentInner(
                comment = comment
            )
        }
    }
}
