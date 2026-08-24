package com.sofamaniac.crabir.ui.post.card

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalFiltersSettings
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.settings.views.ImageHeight
import com.sofamaniac.crabir.ui.markdown.HeightRestrictedWithGradient
import com.sofamaniac.crabir.ui.post.PostGallery
import com.sofamaniac.crabir.ui.post.PostImage
import com.sofamaniac.crabir.ui.post.PostVideo
import com.sofamaniac.crabir.ui.post.StreamableVideo
import com.sofamaniac.crabir.ui.post.YoutubeVideo
import com.sofamaniac.crabir.ui.richtext.Richtext

@Composable
internal fun PostBody(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
    imageHeight: ImageHeight = ImageHeight.Full,
    enableTextPreview: Boolean = true,
    maxLines: Int?,
    enableLinkFullSizePreview: Boolean = true,
    forceShowSelftext: Boolean = true,
    markAsRead: () -> Unit = {},
) {
    val navController = LocalNavController.current
    fun goFullscreen(route: Route) {
        markAsRead()
        navController?.navigate(route)
    }

    val filters = LocalFiltersSettings.current
    val blur = post.spoiler || (post.over18 && filters.blurNSFW)

    val selftextView = @Composable {
        if (maxLines != null) {
            HeightRestrictedWithGradient(
                maxHeight = (24 * maxLines).dp,
                modifier = modifier.padding(horizontal = 16.dp),
                onClick = { goFullscreen(PostRoute(post.permalink)) }
            ) {
                Richtext(post.selftext.richtext, mediaMetadata = post.mediaMetadata)
            }
        } else {
            Richtext(
                post.selftext.richtext,
                mediaMetadata = post.mediaMetadata,
                modifier = modifier.padding(horizontal = 16.dp)
            )
        }
    }
    val screenHeight = LocalWindowInfo.current.containerDpSize.height
    val mediaModifier = modifier
        .fillMaxWidth()
        .then(
            when (imageHeight) {
                ImageHeight.Full -> Modifier
                ImageHeight.Fixed -> Modifier
                    .height(200.dp)
                    .clipToBounds()

                ImageHeight.Screen -> Modifier
                    .heightIn(max = screenHeight.times(0.8f))
                    .clipToBounds()
            }
        )
    when (post.kind) {
        Kind.Image -> {
            PostImage(
                post,
                modifier = mediaModifier,
                goFullscreen = ::goFullscreen,
                blur = blur,
            )
        }

        Kind.Video -> {
            PostVideo(
                post,
                modifier = mediaModifier,
                canPlayVideo = canPlayVideo,
                blur = blur,
                goFullscreen = ::goFullscreen
            )
        }

        Kind.Link -> {
            if (enableLinkFullSizePreview) {
                PostImage(
                    post,
                    modifier.fillMaxWidth(),
                    enabled = false,
                    blur = blur,
                    goFullscreen = ::goFullscreen,
                )
            }
        }

        Kind.Gallery -> {
            PostGallery(
                post,
                modifier.fillMaxWidth(),
                canPlayVideo = canPlayVideo,
                blur = blur,
                goFullscreen = ::goFullscreen,
            )
        }

        Kind.YoutubeVideo -> {
            YoutubeVideo(
                post,
                modifier.fillMaxWidth(),
            )
        }

        Kind.Streamable -> {
            StreamableVideo(
                post,
                canPlayVideo = canPlayVideo,
                modifier = modifier.fillMaxWidth(),
                goFullscreen = ::goFullscreen
            )
        }

        else -> {
            val selftext = post.selftext.markdown
            if (selftext.markdown.isNotBlank() && enableTextPreview) {
                selftextView()
                return
            }
        }
    }
    if (forceShowSelftext) {
        val selftext = post.selftext.markdown
        if (selftext.markdown.isNotBlank() && enableTextPreview) {
            Spacer(modifier = Modifier.height(8.dp))
            selftextView()
        }
    }
}


