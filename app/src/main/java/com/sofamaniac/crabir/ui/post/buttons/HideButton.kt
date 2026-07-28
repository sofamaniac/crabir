package com.sofamaniac.crabir.ui.post.buttons

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.ui.post.LinkInteraction

@Composable
internal fun HideButtonLong(post: PostData, interaction: LinkInteraction, onClick: () -> Unit) {
    ListItem(
        content = {
            val text =
                if (post.relationship.hidden) R.string.unhide_post else R.string.hide_post
            Text(stringResource(text))
        },
        modifier = Modifier.clickable {
            if (post.relationship.hidden) {
                interaction.unhide()
            } else {
                interaction.hide()
            }
            onClick()
        }
    )
}

@Composable
internal fun HideButton(post: PostData, interaction: LinkInteraction) {
    IconButton(onClick = {
        if (post.relationship.hidden) {
            interaction.unhide()
        } else {
            interaction.hide()
        }
    }) {
        val description = if (post.relationship.hidden) R.string.unhide_post else R.string.hide_post
        Icon(Icons.Default.Visibility, stringResource(description))
    }
}
