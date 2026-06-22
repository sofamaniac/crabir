package com.sofamaniac.crabir.ui.post

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sofamaniac.crabir.LocalSharedTransitionScope
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.domain.model.DUMMY_POST
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.navigation.LocalNavController
import com.sofamaniac.crabir.navigation.PostRoute
import com.sofamaniac.crabir.settings.views.rememberViewSettings
import com.sofamaniac.crabir.ui.SharedElementKey
import com.sofamaniac.crabir.ui.SharedElementType
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
    showHidden: Boolean = false,
    animatedContentScope: AnimatedVisibilityScope,
    viewModel: PostViewModelInterface = hiltViewModel<LinkViewModel, LinkViewModel.Factory>(
        key = post.id,
        creationCallback = { factory ->
            factory.create(post)
        }
    ),
) {
    val post by viewModel.post.collectAsState(post)
    val likes by viewModel.likes.collectAsState(post.relationship.liked)
    if (!showHidden && post.relationship.hidden) {
        return
    }
    PostCardContent(
        post,
        modifier,
        clickable,
        markAsRead,
        canStartVideo = canStartVideo,
        read = read,
        likes = likes,
        interactions = viewModel,
        animatedContentScope = animatedContentScope,
    ) {
        OpenThreadButton(
            post,
            onClick = markAsRead
        )
    }
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
    animatedContentScope: AnimatedVisibilityScope,
    interactions: LinkInteraction,
    bottomRowAction: @Composable () -> Unit,
) {

    val settings = rememberViewSettings()
    // We do not apply the padding on the column, but on each of its children except []
    // to have images that take the full width
    val modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(bottom = 4.dp)
    val navController = LocalNavController.current
    val openPost = if (clickable) {
        {
            markAsRead()
            navController?.navigate(PostRoute(post.permalink)) ?: Unit
        }
    } else {
        {}
    }
    val sharedTransitionScope = LocalSharedTransitionScope.current
    with(sharedTransitionScope) {
        val state =
            sharedTransitionScope.rememberSharedContentState(
                key = SharedElementKey(
                    post.name,
                    SharedElementType.Post
                )
            )
        Log.d("PostCardContent", "Match found: ${state.isMatchFound}")
        val animatedModifier = Modifier.sharedElement(
            state,
            animatedVisibilityScope = animatedContentScope
        )

        ThemedCard(
            shape = RoundedCornerShape(0),
            modifier = Modifier
                .fillMaxWidth()
                .then(animatedModifier),
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
                markAsRead = markAsRead,
                animatedVisbilityScope = animatedContentScope,
            )
            BottomRow(post, modifier, interactions = interactions) {
                bottomRowAction()
            }
        }
    }
}

@Preview()
@Composable
internal fun PostCardPreview() {
    val post by DummyInteraction.post.collectAsState()
    AnimatedVisibility(visible = true) {
        PostCardContent(
            post,
            clickable = false,
            markAsRead = {},
            canStartVideo = false,
            read = false,
            likes = post.relationship.liked,
            interactions = DummyInteraction,
            animatedContentScope = this@AnimatedVisibility
        ) {
            OpenThreadButton(
                post,
                onClick = {}
            )
        }
    }
}

object DummyInteraction : LinkInteraction {
    private var _post = MutableStateFlow(DUMMY_POST.copy(kind = Kind.Self))
    override val post: StateFlow<PostData> = _post
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