package com.sofamaniac.crabir.ui.editor.postEditor

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.GalleryItem
import com.sofamaniac.crabir.data.remote.reddit.MediaUploadInterface
import com.sofamaniac.crabir.data.remote.reddit.PostSubmissionBuilder
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.data.remote.reddit.SubmissionBuilderError
import com.sofamaniac.crabir.data.remote.reddit.makeMediaUploadBody
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.RedditAccount
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

abstract class CreatorViewModel(
    protected val api: RedditAPIService,
) : ViewModel() {
    var community: SubredditData? by mutableStateOf(null)
        protected set

    var rules: Rules by mutableStateOf(Rules())
        private set
    var flairs: List<FlairInfo> by mutableStateOf(emptyList())
        private set

    var error: SubmissionBuilderError? by mutableStateOf(null)
        protected set

    val titleState = TextFieldState()

    suspend fun getRules() {
        if (rules.siteRules.isNotEmpty()) return
        val res = api.getRules(community!!.displayNamePrefixed)
        if (res.isSuccess) {
            rules = res.getOrNull()!!
        }
    }

    fun setSubreddit(subreddit: SubredditData) {
        viewModelScope.launch {
            community = subreddit
            getRules()
            getFlairs()
        }
    }


    fun resetSubreddit() {
        community = null
    }

    suspend fun getFlairs() {
        if (flairs.isNotEmpty() || community == null) return
        val res = api.getPostFlair(community!!.displayNamePrefixed)
        if (res.isSuccess) {
            flairs = res.getOrNull()!!
        }
    }
}

@KoinViewModel
class PostCreatorViewModel(
    api: RedditAPIService,
    private val communities: SubredditDao,
    private val mediaUploader: MediaUploadInterface,
    accountsRepository: AccountsRepository,
) : CreatorViewModel(api) {
    var state by mutableStateOf(PostSubmissionBuilder())
    val textState = TextFieldState()
    val urlState = TextFieldState()

    var media: List<Uri> by mutableStateOf(emptyList())
    var captions: MutableMap<Uri, String> = mutableMapOf()
    var loading by mutableStateOf(false)

    val accounts: Flow<List<RedditAccount>> = accountsRepository.accounts

    fun setKind(context: Context) {
        if (media.size > 1) {
            Log.d("PostCreatorViewModel", "setKind: gallery")
            state = state.copy(kind = Kind.Gallery)
        } else if (media.size == 1) {
            val type = getMimeType(media[0], context)
            state = if (type.startsWith("image")) {
                Log.d("PostCreatorViewModel", "setKind: image")
                state.copy(kind = Kind.Image)
            } else {
                Log.d("PostCreatorViewModel", "setKind: video")
                state.copy(kind = Kind.Video)
            }
        }
    }

    fun setSubreddit(subreddit: String) {
        viewModelScope.launch {
            val sub = communities.getBySlug(subreddit)
            if (sub == null) return@launch
            community = sub
            getRules()
            getFlairs()
        }
    }

    class MediaUploadError(message: String) : Exception(message)

    suspend fun uploadMedia(
        context: Context,
        kind: String = "link",
        account: RedditAccount?,
    ): List<String> {
        if (media.isNotEmpty()) {
            val uploadResponse = media.map {
                Log.d("PostCreatorViewModel", "submit: uploading $it")
                val mimetype = getMimeType(it, context)
                val result = api.uploadMedia(it.toString(), mimetype, account)
                if (result.isSuccess) {
                    result.getOrNull()!!
                } else {
                    throw MediaUploadError("Failed to upload media")
                }
            }
            return uploadResponse.mapIndexed { index, args ->
                val request = makeMediaUploadBody(context, media[index], args.args.fields)
                val uploadUrl = "https:${args.args.action}"
                val response = mediaUploader.pushMedia(uploadUrl, request)
                if (response.isSuccess) {
                    if (kind == "link") {
                        response.getOrNull()?.location!!
                    } else {
                        args.asset.assetId
                    }
                } else {
                    throw MediaUploadError("Failed to upload media")
                }
            }
        } else {
            return emptyList()
        }
    }

    suspend fun submit(context: Context, account: RedditAccount?): Result<Unit> {
        loading = true
        state = state.copy(
            title = titleState.text as String,
            text = textState.text as String,
            url = urlState.text as String,
            subreddit = community?.displayName ?: ""
        )
        setKind(context)
        var submission = state.build()
        if (submission.isFailure) {
            Log.e("PostCreatorViewModel", "submit: ${submission.exceptionOrNull()}")
            error = submission.exceptionOrNull() as SubmissionBuilderError?
            loading = false
            return Result.failure(error!!)
        } else {
            val mediaIds = uploadMedia(
                context,
                kind = if (state.kind == Kind.Gallery) "gallery" else "link",
                account,
            )
            val items = mediaIds.map {
                GalleryItem(mediaId = it)
            }
            if (mediaIds.size == 1) {
                state = state.copy(url = mediaIds[0])
                submission = state.build()
            }
            val res = if (state.kind == Kind.Gallery) {
                val gallery = state.toGallerySubmission()
                val galleryFinal = gallery.copy(items = items)
                api.submitGalleryPost(galleryFinal, account)
            } else {
                api.submitPost(submission.getOrThrow(), account)
            }
            loading = false
            return if (res.isSuccess) {
                val response = res.getOrNull()
                Log.d("PostCreatorViewModel", "submit: $response")
                if (response?.json?.errors?.isNotEmpty() == true) {
                    Result.failure(Exception(response.json.errors.toString()))
                } else {
                    Result.success(Unit)
                }
            } else {
                Result.failure(Exception("Failed to submit post: ${res.exceptionOrNull()}"))
            }
        }
    }
}

@OptIn(UnstableApi::class)
fun getMimeType(uri: Uri, context: Context): String {
    val type = context.contentResolver.getType(uri)
    Log.d("PostCreatorViewModel", "getMimeType: $type ($uri)")
    return type!!
}
