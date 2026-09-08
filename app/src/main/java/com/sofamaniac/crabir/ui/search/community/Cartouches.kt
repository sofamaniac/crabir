package com.sofamaniac.crabir.ui.search.community

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.sofamaniac.crabir.R
import com.sofamaniac.crabir.ui.components.cartouche

val CartoucheColor = Color(0xFF0097A7)

@Composable
fun PrivateCartouche() {
    Text(
        stringResource(R.string.subreddit_private),
        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
        modifier = Modifier.cartouche(CartoucheColor)
    )
}

@Composable
fun RestrictedCartouche() {
    Text(
        stringResource(R.string.subreddit_restricted),
        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
        modifier = Modifier.cartouche(CartoucheColor)
    )
}
