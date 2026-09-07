package com.sofamaniac.crabir.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

fun Modifier.cartouche(
    backgroundColor: Color,
    shape: RoundedCornerShape = RoundedCornerShape(2.dp),
) =
    this
        .background(color = backgroundColor, shape = shape)
        .padding(horizontal = 4.dp)


@Composable
fun Over18Cartouche() {
    Text(
        "NSFW", fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
        modifier = Modifier.cartouche(Color.Red)
    )
}

@Composable
fun SpoilerCartouche() {
    Text(
        "SPOILER",
        style = MaterialTheme.typography.labelSmall.copy(color = Color.Red),
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Red,
                shape = RoundedCornerShape(corner = CornerSize(2.dp)),
            )
            .cartouche(Color.Transparent)
    )
}
