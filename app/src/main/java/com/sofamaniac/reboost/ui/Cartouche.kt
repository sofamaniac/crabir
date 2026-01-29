package com.sofamaniac.reboost.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Cartouche(
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(corner = CornerSize(2.dp)),
        modifier = modifier,
    ) {
        Box(modifier = Modifier.padding(horizontal = 4.dp)) {
            content()
        }
    }
}