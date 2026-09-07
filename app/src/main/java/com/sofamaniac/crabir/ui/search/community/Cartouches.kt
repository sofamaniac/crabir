package com.sofamaniac.crabir.ui.search.community

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sofamaniac.crabir.ui.cartouche

val CartoucheColor = Color.Cyan

@Composable
fun PrivateCartouche() {
    Text(
        "PRIVATE",
        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
        modifier = Modifier.cartouche(CartoucheColor)
    )
}

@Composable
fun RestrictedCartouche() {
    Text(
        "RESTRICTED",
        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
        modifier = Modifier.cartouche(CartoucheColor)
    )
}
