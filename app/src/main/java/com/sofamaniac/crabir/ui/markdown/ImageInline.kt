package com.sofamaniac.crabir.ui.markdown

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sofamaniac.crabir.data.remote.dto.post.MediaMetadata
import com.sofamaniac.crabir.domain.model.MediaResource
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.glide.GlideImagesPlugin

class ImageInline(context: Context, val mediaMetadata: Map<String, MediaMetadata>) :
    GlideImagesPlugin.GlideStore {
    private val requestManager = Glide.with(context).apply {
        addDefaultRequestListener(
            object : RequestListener<Any> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Any>,
                    isFirstResource: Boolean
                ): Boolean = false

                override fun onResourceReady(
                    resource: Any,
                    model: Any,
                    target: Target<Any>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    (resource as? GifDrawable)?.start()
                    return false
                }
            }
        )
    }


    override fun load(drawable: AsyncDrawable): RequestBuilder<Drawable?> {
        val metadata = drawable.getMetadata(mediaMetadata)
        val placeholder =
            Color.GRAY.toDrawable()
        if (metadata != null) {
            placeholder.setBounds(
                0,
                0,
                metadata.width,
                metadata.height
            )
        }

        return requestManager
            .load(metadata?.url ?: drawable.destination)
    }

    override fun cancel(target: Target<*>) {
        requestManager.clear(target)
    }
}

private fun AsyncDrawable.getMetadata(mediaMetadata: Map<String, MediaMetadata>): MediaResource? {
    // Handle destination of the form `giphy|xxxx`
    if (destination.contains('|')) {
        val metadata = mediaMetadata[destination]
        if (metadata != null && metadata !is MediaMetadata.Invalid) {
            return metadata.toMediaResource()
        } else {
            val id = destination.split('|').last()
            return MediaResource("https://media.giphy.com/media/$id/giphy.gif", 1f, 100, 100)
        }
    }
    // Otherwise assume destination is a link
    val url = destination.toUri()
    val filename = url.pathSegments.lastOrNull()?.split('.')?.firstOrNull()
    return mediaMetadata[filename]?.toMediaResource()
}
