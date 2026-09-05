package com.sofamaniac.crabir.ui.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.ui.formatElapsedTimeLocalized
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
fun AboutTab(user: UserDTO?, modifier: Modifier = Modifier) {
    if (user == null) return

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top) {
        Text(user.subreddit.publicDescription)
        Spacer(Modifier.height(8.dp))
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("KARMA")
                Text("${user.totalKarma}")
                Row {
                    Icon(Icons.Default.Link, contentDescription = "Link karma")
                    Text("${user.linkKarma}")
                    Icon(Icons.Outlined.ModeComment, contentDescription = "Comment karma")
                    Text("${user.commentKarma}")
                }
                Row {
                    Icon(Icons.Default.CardGiftcard, contentDescription = "Awarder karma")
                    Text("${user.awarderKarma}")
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
                    .ofPattern("MMM dd, yyyy")
                    .withLocale(LocalLocale.current.platformLocale)
                    .withZone(ZoneId.systemDefault())

                Text("REDDIT AGE")
                Text(formatElapsedTimeLocalized(created))
                Row {
                    Icon(Icons.Default.Cake, contentDescription = null)
                    Text(formatter.format(created.toJavaInstant()))
                }
            }
        }
    }
}
