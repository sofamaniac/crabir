package com.sofamaniac.crabir.ui.drawer.buttons

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import com.sofamaniac.crabir.LocalRedditAccount
import com.sofamaniac.crabir.navigation.InboxRoute
import com.sofamaniac.crabir.navigation.ProfileRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.lateralMenu.LateralMenuItems

internal fun LazyListScope.profileButtons(settings: LateralMenuItems, onClick: (Route) -> Unit) {
    if (settings.profile) {
        item {
            val currentAccount = LocalRedditAccount.current
            NavigationDrawerItem(
                label = { Text("Profile") },
                selected = false,
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                onClick = {
                    val username = currentAccount.info?.username
                    if (username != null) {
                        onClick(ProfileRoute(username))
                    }
                }
            )
        }
    }
    if (settings.inbox) {
        item {
            NavigationDrawerItem(
                label = { Text("Inbox") },
                selected = false,
                icon = { Icon(Icons.Default.Inbox, contentDescription = null) },
                onClick = {
                    onClick(InboxRoute)
                }
            )
        }
    }
    if (settings.friends) {
        item {
            NavigationDrawerItem(
                label = { Text("Friends") },
                selected = false,
                icon = { Icon(Icons.Default.Group, contentDescription = null) },
                onClick = {
                }
            )
        }
    }
    if (settings.drafts) {
        item {
            NavigationDrawerItem(
                label = { Text("Drafts") },
                selected = false,
                icon = { Icon(Icons.Default.Drafts, contentDescription = null) },
                onClick = {
                }
            )
        }
    }
    if (settings.moderation) {
        item {
            NavigationDrawerItem(
                label = { Text("Moderation") },
                selected = false,
                icon = { Icon(Icons.Default.Shield, contentDescription = null) },
                onClick = {
                }
            )
        }
    }
}