package com.sofamaniac.crabir.ui.markdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.mikepenz.markdown.compose.LocalImageTransformer

@Composable
fun ClickableMarkdownInlineImage(
    link: String,
    linkHandler: LinkInteractionListener?,
) {
    when (link.toUri().host) {
        "v.redd.it" -> InlineVideo(link, linkHandler)
        else -> InlineImage(link, linkHandler)
    }
}

@Composable
private fun InlineVideo(
    link: String,
    linkHandler: LinkInteractionListener?,
) {
    val transformer = LocalImageTransformer.current
    transformer.transform(link)?.let { imageData ->
        val modifier = Modifier
            .fillMaxSize()
            .then(imageData.modifier)
            .clickable {
                linkHandler?.onClick(LinkAnnotation.Url(link))
            }
        Box(modifier = modifier.then(imageData.modifier)) {
            Image(
                painter = imageData.painter,
                contentDescription = imageData.contentDescription,
                modifier = Modifier.fillMaxSize(),
                alignment = imageData.alignment,
                contentScale = imageData.contentScale,
                alpha = imageData.alpha,
                colorFilter = imageData.colorFilter
            )

            Icon(
                Icons.Default.PlayArrow, contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun InlineImage(
    link: String,
    linkHandler: LinkInteractionListener?,
) {
    val transformer = LocalImageTransformer.current
    transformer.transform(link)?.let { imageData ->
        val modifier = Modifier
            .fillMaxSize()
            .then(imageData.modifier)
            .clickable {
                linkHandler?.onClick(LinkAnnotation.Url(link))
            }
        Image(
            painter = imageData.painter,
            contentDescription = imageData.contentDescription,
            modifier = modifier.then(imageData.modifier),
            alignment = imageData.alignment,
            contentScale = imageData.contentScale,
            alpha = imageData.alpha,
            colorFilter = imageData.colorFilter
        )
    }
}
