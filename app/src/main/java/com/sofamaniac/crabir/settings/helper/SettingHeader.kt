package com.sofamaniac.crabir.settings.helper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalTheme

@Composable
fun SettingHeader(title: String) {
    val theme = LocalTheme.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = theme.cardBackground),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            title,
            color = theme.highlight,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 32.dp)
        )
        HorizontalDivider()
    }
}