package com.sofamaniac.crabir.ui.markdown

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.mikepenz.markdown.compose.LocalImageTransformer
import com.sofamaniac.crabir.navigation.LocalNavController
import org.intellij.markdown.ast.ASTNode

@Composable
fun ClickableMarkdownInlineImage(link: String, node: ASTNode) {
    val transformer = LocalImageTransformer.current
    val uriHandler = LocalUriHandler.current
    val navController = LocalNavController.current!!
    transformer.transform(link)?.let { imageData ->
        val modifier = Modifier
            .fillMaxSize()
            .then(imageData.modifier)
            .clickable {
                try {
                    navController.navigate(link)
                } catch (e: IllegalArgumentException) {
                    Log.i("ClickableMarkdownInlineImage", "ClickableMarkdownInlineImage: $e")
                    uriHandler.openUri(link)
                }
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