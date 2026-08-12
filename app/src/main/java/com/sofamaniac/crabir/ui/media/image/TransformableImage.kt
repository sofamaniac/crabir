package com.sofamaniac.crabir.ui.media.image

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.SingletonImageLoader
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.transformation.blur.BlurTransformationPlugin
import com.skydoves.landscapist.zoomable.ZoomablePlugin
import com.skydoves.landscapist.zoomable.rememberZoomableState


@Composable
fun TransformableImage(
    source: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    allowZoom: Boolean = true,
    blur: Boolean = false,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderAspectRatio: Float? = null,
    onZoomChange: (Float) -> Unit = {},
    onClick: () -> Unit = {},
) {
    val state = rememberZoomableState()

    LaunchedEffect(state.transformation.scale) {
        onZoomChange(state.transformation.scale.scaleX)
    }
    Log.d("TransformableImage", "aspect ratio: $placeholderAspectRatio")

    //    val modifier = when (placeholderAspectRatio) {
    //        null -> modifier
    //        else -> modifier.aspectRatio(placeholderAspectRatio)
    //    }

    CoilImage(
        modifier = modifier,
        imageLoader = {
            val context = LocalContext.current
            SingletonImageLoader.get(context)
        },
        imageModel = {
            source
        },
        imageOptions = ImageOptions(
            contentScale = contentScale,
            contentDescription = contentDescription,
            placeholderAspectRatio = placeholderAspectRatio
        ),
        loading = {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        },
        component = rememberImageComponent {
            +ZoomablePlugin(
                state,
                onTap = {
                    onClick()
                },
                enabled = allowZoom,
            )
            if (blur) {
                +BlurTransformationPlugin(radius = 20)
            }
        }
    )
}