package com.sofamaniac.crabir.ui.thread.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.votable.CommentData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.routes.ProfileRoute
import com.sofamaniac.crabir.settings.theme.ADMIN_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.AUTHOR_CARTOUCHE_COLOR
import com.sofamaniac.crabir.settings.theme.MODERATOR_CARTOUCHE_COLOR
import com.sofamaniac.crabir.ui.components.Flair
import com.sofamaniac.crabir.ui.components.cartouche
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import com.sofamaniac.crabir.ui.user.ProfileTabs
import com.sofamaniac.crabir.ui.votable.ScoreString

@Composable
fun TopRow(comment: CommentData, modifier: Modifier = Modifier.Companion) {

    val theme = LocalTheme.current
    val timeString = formatElapsedTimeLocalized(comment.createdUtc)

    val navController = LocalNavController.current!!
    val authorModifier = Modifier.clickable {
        navController.navigate(
            ProfileRoute(
                comment.author.username,
                ProfileTabs.Overview
            )
        )
    }.let {
        if (comment.isSubmitter) {
            it.cartouche(colorResource(AUTHOR_CARTOUCHE_COLOR))
        } else if (comment.distinguished == "moderator") {
            it.cartouche(colorResource(MODERATOR_CARTOUCHE_COLOR))
        } else if (comment.distinguished == "admin") {
            it.cartouche(colorResource(ADMIN_CARTOUCHE_COLOR))
        } else {
            it
        }
    }
    val authorString = comment.author.username
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                authorString,
                modifier = authorModifier,
                color = if (comment.isSubmitter || !comment.distinguished.isNullOrBlank()) Color.White else theme.highlight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (comment.locked) {
                Text(stringResource(R.string.post_locked))
            }
            if (comment.stickied) {
                Icon(painterResource(R.drawable.ic_stickied), contentDescription = "Stickied")
            }
            Flair(comment.author.flair)
            Spacer(modifier = Modifier.weight(1f))
        }
        ScoreString(
            comment.score.score,
            comment.relationship.liked,
            hidden = comment.score.hideScore
        )
        Text(stringResource(R.string.text_separator))
        Text(timeString, maxLines = 1)
    }
}
