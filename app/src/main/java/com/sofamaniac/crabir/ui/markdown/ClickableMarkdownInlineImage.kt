package com.sofamaniac.crabir.ui.markdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.mikepenz.markdown.compose.LocalImageTransformer
import org.intellij.markdown.ast.ASTNode

@Composable
fun ClickableMarkdownInlineImage(link: String, node: ASTNode) {
    val transformer = LocalImageTransformer.current
    val uriHandler = LocalUriHandler.current
    transformer.transform(link)?.let { imageData ->
        val modifier = Modifier.Companion
            .fillMaxSize()
            .then(imageData.modifier)
            .clickable {
                uriHandler.openUri(link)
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