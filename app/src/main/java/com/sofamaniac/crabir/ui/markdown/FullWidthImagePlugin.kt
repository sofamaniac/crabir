package com.sofamaniac.crabir.ui.markdown

import android.graphics.Rect
import androidx.core.net.toUri
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.ImageSize
import io.noties.markwon.image.ImageSizeResolverDef

/** Ensure images take up the full width of the canvas. */
class FullWidthImagePlugin(val mediaMetadata: Map<String, MediaMetadata>) :
    AbstractMarkwonPlugin() {
    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.imageSizeResolver(object : ImageSizeResolverDef() {
            override fun resolveImageSize(drawable: AsyncDrawable): Rect {
                val url = drawable.destination.toUri()
                val filename = url.pathSegments.last().split('.').first()
                val metadata = mediaMetadata[filename]
                return resolveImageSize(
//                    ImageSize(
//                        ImageSize.Dimension(metadata!!.width.toFloat(), UNIT_EM),
//                        ImageSize.Dimension(metadata!!.height.toFloat(), UNIT_EM)
//                    ),
                    ImageSize(
                        ImageSize.Dimension(100f, UNIT_PERCENT),
                        metadata?.let {
                            ImageSize.Dimension(100f * it.ratio, UNIT_PERCENT)
                        }
                    ),
                    drawable.result.bounds,
                    drawable.lastKnownCanvasWidth,
                    drawable.lastKnowTextSize
                )
            }

        })
    }

}