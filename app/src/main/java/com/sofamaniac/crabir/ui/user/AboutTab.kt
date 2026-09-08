package com.sofamaniac.crabir.ui.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
fun AboutTab(user: UserDTO?, modifier: Modifier = Modifier) {
    if (user == null) return
    val theme = LocalTheme.current

    Column(
        modifier
            .fillMaxSize()
            .background(color = theme.cardBackground),
        verticalArrangement = Arrangement.Top
    ) {
        Text(user.subreddit.publicDescription)
        Spacer(Modifier.height(16.dp))
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column {
                Text(stringResource(R.string.karma))
                Text("${user.totalKarma}")
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Link, contentDescription = "Link karma")
                    Text("${user.linkKarma}")
                    Spacer(Modifier.size(8.dp))
                    Icon(Icons.Outlined.ModeComment, contentDescription = "Comment karma")
                    Text("${user.commentKarma}")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = "Awarder karma")
                    Text("${user.awarderKarma}")
                    Spacer(Modifier.size(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.CallReceived,
                        contentDescription = "Awardee karma"
                    )
                    Text("${user.awardeeKarma}")
                }
            }
            Column {
                val created = Instant.fromEpochSeconds(user.createdUtc.toLong())
                val formatter = DateTimeFormatter
                    .ofPattern(stringResource(R.string.reddit_account_creation_format))
                    .withLocale(LocalLocale.current.platformLocale)
                    .withZone(ZoneId.systemDefault())

                Text(stringResource(R.string.reddit_age))
                Text(formatElapsedTimeLocalized(created))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.Cake, contentDescription = null)
                    Text(formatter.format(created.toJavaInstant()))
                }
            }
        }
    }
}
