package com.sofamaniac.reboost.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.cartouche(
    backgroundColor: Color,
    shape: RoundedCornerShape = RoundedCornerShape(2.dp)
) =
    this
        .background(color = backgroundColor, shape = shape)
        .padding(horizontal = 4.dp)