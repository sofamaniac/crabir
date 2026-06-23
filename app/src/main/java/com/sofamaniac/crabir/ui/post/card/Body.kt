package com.sofamaniac.crabir.ui.post.card

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sofamaniac.crabir.LocalSharedTransitionScope
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.Route
import com.sofamaniac.crabir.ui.SharedElementKey
import com.sofamaniac.crabir.ui.SharedElementType
import com.sofamaniac.crabir.ui.markdown.RedditMarkdown
import com.sofamaniac.crabir.ui.post.PostGallery
import com.sofamaniac.crabir.ui.post.PostImage
import com.sofamaniac.crabir.ui.post.PostVideo
import com.sofamaniac.crabir.ui.post.StreamableVideo
import com.sofamaniac.crabir.ui.post.YoutubeVideo
import com.mikepenz.markdown.model.State as MarkdownState

@Composable
internal fun PostBody(
    post: PostData,
    modifier: Modifier = Modifier,
    canPlayVideo: Boolean = false,
    enableFullHeightImage: Boolean = true,
    enableTextPreview: Boolean = true,
    maxLines: Int?,
    enableLinkFullSizePreview: Boolean = true,
    forceShowSelftext: Boolean = false,
    animatedVisibilityScope: AnimatedVisibilityScope,
    markdownState: MarkdownState,
    markAsRead: () -> Unit = {},
) {
    val navController = LocalNavController.current!!
    fun goFullscreen(route: Route) {
        markAsRead()
        navController.navigate(route)
    }

    val sharedTransitionScope = LocalSharedTransitionScope.current
    with(sharedTransitionScope) {
        val modifier =
            modifier.sharedElement(
                sharedTransitionScope.rememberSharedContentState(
                    key = SharedElementKey(
                        post.name,
                        SharedElementType.Content
                    )
                ),
                animatedVisibilityScope
            )
        val selftextView = @Composable {
            RedditMarkdown(
                markdownState,
                maxLines = maxLines,
                modifier = modifier.padding(horizontal = 16.dp),
                mediaMetadata = post.mediaMetadata,
                //onClick = { goFullscreen(PostRoute(post.permalink, null)) }
            )
        }
        when (post.kind) {
            Kind.Image -> {
                PostImage(
                    post,
                    modifier.fillMaxWidth(),
                    goFullscreen = ::goFullscreen,
                )
            }

            Kind.Video -> {
                PostVideo(
                    post,
                    modifier.fillMaxWidth(),
                    canPlayVideo = canPlayVideo,
                    goFullscreen = ::goFullscreen
                )
            }

            Kind.Link -> {
                if (enableLinkFullSizePreview) {
                    PostImage(
                        post,
                        modifier.fillMaxWidth(),
                        enabled = false,
                        goFullscreen = ::goFullscreen,
                    )
                }
            }

            Kind.Gallery -> {
                PostGallery(
                    post,
                    modifier.fillMaxWidth(),
                    canPlayVideo = canPlayVideo,
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
                selftextView()
            }
        }
    }
}


