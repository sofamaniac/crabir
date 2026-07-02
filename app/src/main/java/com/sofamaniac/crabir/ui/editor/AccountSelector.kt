package com.sofamaniac.crabir.ui.editor

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.domain.model.RedditAccount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSelector(
    accounts: List<RedditAccount>,
    activeAccount: RedditAccount,
    onAccountSelection: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        AccountTile(
            activeAccount,
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            for (account in accounts) {
                if (account == activeAccount || account.isAnonymous() || account.isUninitialized()) continue
                AccountTile(
                    account,
                    onClick = { onAccountSelection(account.id) }
                )

            }
        }
    }
}

@Composable
private fun AccountTile(
    account: RedditAccount,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    if (account.info == null) return
    DropdownMenuItem(
        text = { Text(account.info.username) },
        onClick = onClick,
        leadingIcon = {
            AsyncImage(
                model = account.info.iconImg,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        },
        trailingIcon = trailingIcon,
        modifier = modifier,
    )
}