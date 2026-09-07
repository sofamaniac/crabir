package com.sofamaniac.crabir.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sofamaniac.crabir.data.remote.dto.MultiData

@Composable
fun MultiIcon(multi: MultiData, contentDescription: String? = null) {
    AsyncImage(
        multi.iconUrl,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
    )
}
