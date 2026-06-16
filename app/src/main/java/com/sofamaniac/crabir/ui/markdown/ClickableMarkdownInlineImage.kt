package com.sofamaniac.crabir.ui.markdown

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.LinkInteractionListener
import com.mikepenz.markdown.compose.LocalImageTransformer
import org.intellij.markdown.ast.ASTNode

@Composable
fun ClickableMarkdownInlineImage(
    link: String,
    node: ASTNode,
    linkHandler: LinkInteractionListener?
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