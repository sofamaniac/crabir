package com.sofamaniac.crabir.ui.votable

import android.icu.text.CompactDecimalFormat
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.sofamaniac.crabir.LocalTheme

@Composable
fun ScoreString(score: Int, likes: Boolean?, hidden: Boolean = false) {
    val scoreStyle =
        MaterialTheme.typography.titleMedium.toSpanStyle()
    val locale = LocalConfiguration.current.locales[0] ?: LocalLocale.current.platformLocale
    val formatter = remember(locale) {
        CompactDecimalFormat.getInstance(locale, CompactDecimalFormat.CompactStyle.SHORT)
    }
    val text = buildAnnotatedString {
        withStyle(style = scoreStyle) {
            if (hidden)
                append("?")
            else
                append(formatter.format(score))
        }
    }

    val theme = LocalTheme.current
    val color by animateColorAsState(
        targetValue = when (likes) {
            true -> theme.primaryColor
            false -> theme.downvote
            else -> theme.secondaryText
        },
        label = "score color"
    )
    var previousLike by remember { mutableStateOf(likes) }

    val scale = remember { Animatable(1f) }
    LaunchedEffect(likes) {
        val trigger = previousLike != true
        previousLike = likes

        if (likes == true && trigger) {
            scale.animateTo(1.7f, animationSpec = tween(100, easing = EaseOut))
            scale.animateTo(1f, animationSpec = tween(100, easing = EaseIn))
        }
    }

    Text(text, color = color, modifier = Modifier.scale(scale.value))
}
