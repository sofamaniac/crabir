package com.sofamaniac.crabir.ui.post

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.DUMMY_POST
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.settings.views.rememberViewSettings
import com.sofamaniac.crabir.ui.ThemedCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

/**
 * Composable function that displays a single post in a Card format.
 *
 * This function creates a view for a given [Post], including its header,
 * content, and bottom row of actions.
 *
 * @param post The [com.sofamaniac.crabir.domain.model.PostData] data to display.
 * @param modifier Modifier for the root layout of the post.
 * @param enableThumbnail Whether to enable the thumbnail preview. Defaults to true. The thumbnail is shown only if there is one and the post if a link.
 * @param showSubredditIcon Whether to display the subreddit icon in the header. Defaults to true.
 * @param clickable Whether the post is clickable to navigate to the thread view. Defaults to true.
 * @param markAsRead A lambda that takes a [com.sofamaniac.crabir.domain.model.PostData] and is called before navigating to the post.
 * @param canStartVideo Whether the post can start a video. Defaults to false.
 */
@Composable
fun PostCard(
    post: PostData,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    markAsRead: () -> Unit = {},
    canStartVideo: Boolean = false,
    read: Boolean = false,
    viewModel: LinkViewModel = hiltViewModel<LinkViewModel, LinkViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post)
        }),
) {
    val _post by viewModel.post.collectAsState()
    val likes by viewModel.likes.collectAsState(null)
    if (_post == null) {
        Log.w("PostCard", "Trying to render null")
        return
    }
    val post = _post!!
    PostCardContent(
        post,
        modifier,
        clickable,
        markAsRead,
        canStartVideo = canStartVideo,
        read = read,
        likes = likes,
        interactions = viewModel,
    )
}

@Composable
internal fun PostCardContent(
    post: PostData,
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    markAsRead: () -> Unit = {},
    canStartVideo: Boolean = false,
    read: Boolean = false,
    likes: Boolean?,
    interactions: LinkInteraction,
) {

    val settings = rememberViewSettings()
    // We do not apply the padding on the column, but on each of its children except []
    // to have images that take the full width
    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    val navController = LocalNavController.current!!
    val openPost = if (clickable) {
        {
            markAsRead()
            navController.navigate(PostRoute(post.permalink))
        }
    } else {
        {}
    }
    ThemedCard(
        shape = RoundedCornerShape(0),
        modifier = Modifier.fillMaxWidth(),
        onClick = openPost,
    ) {
        PostHeader(
            post,
            showSubredditIcon = settings.cardSettings.showSubredditIcon,
            modifier = modifier.padding(vertical = 8.dp),
            showPrefix = settings.prefixCommunity
        )
        val enablePreview = post.kind == Kind.Link || post.kind == Kind.Unknown
        PostInfo(
            post,
            modifier = modifier,
            enableThumbnail = enablePreview && settings.cardSettings.thumbnailForLinkPreview,
            likes = likes,
            read = read,
            markAsRead = markAsRead
        )
        PostBody(
            post,
            canPlayVideo = canStartVideo,
            enableFullHeightImage = settings.cardSettings.enableFullHeightImage,
            enableTextPreview = settings.cardSettings.enableTextPreview && !post.spoiler,
            maxLines = settings.cardSettings.maxLines,
            enableLinkFullSizePreview = !settings.cardSettings.thumbnailForLinkPreview,
            markAsRead = markAsRead
        )
        BottomRow(post, modifier, interactions = interactions) {
            OpenThreadButton(
                post,
                onClick = markAsRead
            )
        }
    }
}

@Preview()
@Composable
internal fun PostCardPreview() {
    val navController = rememberNavController()
    val post by DummyInteraction.post.collectAsState()
    CompositionLocalProvider(LocalNavController provides navController) {
        PostCardContent(
            post!!,
            clickable = false,
            markAsRead = {},
            canStartVideo = false,
            read = false,
            likes = null,
            interactions = DummyInteraction
        )
    }
}

object DummyInteraction : LinkInteraction {
    private var _post = MutableStateFlow(DUMMY_POST.copy(kind = Kind.Self))
    override val post: StateFlow<PostData?> = _post
    override val flairs: StateFlow<List<FlairInfo>> = MutableStateFlow(emptyList())

    override fun hide() {
    }

    override fun unhide() {
    }

    override fun delete() {
    }

    override fun editFlair(flairId: String, text: String?) {
    }

    override fun getFlairs() {
    }

    override fun markNSFW() {
        _post.value = _post.value.copy(over18 = true)
    }

    override fun unmarkNSFW() {
        _post.value = _post.value.copy(over18 = false)
    }

    override fun markSpoiler() {
        _post.value = _post.value.copy(spoiler = true)
    }

    override fun unmarkSpoiler() {
        _post.value = _post.value.copy(spoiler = false)
    }

    override fun setInboxReplies(enabled: Boolean) {
    }

    override val likes: Flow<Boolean?> = _post.map { it.relationship.liked }
    override val saved: Flow<Boolean> = _post.map { it.relationship.saved }
    override val rules: StateFlow<Rules> = MutableStateFlow(Rules())

    override fun upvote(name: Fullname) {
        val likes = _post.value.relationship.liked
        var relationship = _post.value.relationship
        var score = _post.value.score
        if (likes == true) {
            relationship = relationship.copy(liked = null)
            score = score.copy(ups = score.ups - 1, score = score.score - 1)
        } else {
            relationship = relationship.copy(liked = true)
            score = score.copy(ups = score.ups + 1, score = score.score + 1)
        }
        _post.value =
            _post.value.copy(
                relationship = relationship,
                score = score
            )
    }

    override fun downvote(name: Fullname) {
        val likes = _post.value.relationship.liked
        var relationship = _post.value.relationship
        var score = _post.value.score
        if (likes == false) {
            relationship = relationship.copy(liked = null)
            score = score.copy(downs = score.downs - 1, score = score.score + 1)
        } else {
            relationship = relationship.copy(liked = false)
            score = score.copy(downs = score.downs + 1, score = score.score - 1)
        }
        _post.value =
            _post.value.copy(
                relationship = relationship,
                score = score
            )
    }

    override fun save(name: Fullname, target: Boolean) {
        _post.value = _post.value.copy(relationship = _post.value.relationship.copy(saved = target))
    }

    override fun fetchRules() {
    }

    override fun report(reason: String) {
    }
}