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
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.remote.reddit.FlairInfo
import com.sofamaniac.crabir.data.remote.reddit.GalleryItem
import com.sofamaniac.crabir.data.remote.reddit.MediaUploadInterface
import com.sofamaniac.crabir.data.remote.reddit.PostSubmissionBuilder
import com.sofamaniac.crabir.data.remote.reddit.RedditAPIService
import com.sofamaniac.crabir.data.remote.reddit.Rules
import com.sofamaniac.crabir.data.remote.reddit.SubmissionBuilderError
import com.sofamaniac.crabir.data.remote.reddit.makeMediaUploadBody
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.SubredditData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class CreatorViewModel(
    protected val api: RedditAPIService,
    private val communities: SubredditRepository
) : ViewModel() {
    var community: SubredditData? by mutableStateOf(null)
        private set

    var rules: Rules by mutableStateOf(Rules())
        private set
    var flairs: List<FlairInfo> by mutableStateOf(emptyList())
        private set

    var error: SubmissionBuilderError? by mutableStateOf(null)
        protected set

    val titleState = TextFieldState()

    suspend fun getRules() {
        if (rules.siteRules.isNotEmpty()) return
        val res = api.getRules(community!!.displayName)
        if (res.isSuccessful) {
            rules = res.body()!!
        }
    }

    fun setSubreddit(subreddit: SubredditData) {
        viewModelScope.launch {
            community = subreddit
            getRules()
            getFlairs()
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

    suspend fun getFlairs() {
        if (flairs.isNotEmpty() || community == null) return
        val res = api.getPostFlair(community!!.displayName)
        if (res.isSuccessful) {
            flairs = res.body()!!
        }
    }
}

@HiltViewModel
class PostCreatorViewModel @Inject constructor(
    api: RedditAPIService,
    communities: SubredditRepository,
    private val mediaUploader: MediaUploadInterface
) : CreatorViewModel(api, communities) {
    var state by mutableStateOf(PostSubmissionBuilder())
    val textState = TextFieldState()
    val urlState = TextFieldState()

    var media: List<Uri> by mutableStateOf(emptyList())
    var captions: MutableMap<Uri, String> = mutableMapOf()
    var loading by mutableStateOf(false)

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

    suspend fun uploadMedia(context: Context, kind: String = "link"): List<String> {
        if (media.isNotEmpty()) {
            val uploadResponse = media.map {
                Log.d("PostCreatorViewModel", "submit: uploading $it")
                val mimetype = getMimeType(it, context)
                val result = api.uploadMedia(it.toString(), mimetype)
                if (result.isSuccessful) {
                    result.body()!!
                } else {
                    throw Exception("Failed to upload media")
                }
            }
            return uploadResponse.mapIndexed { index, args ->
                val request = makeMediaUploadBody(context, media[index], args.args.fields)
                val uploadUrl = "https:${args.args.action}"
                val response = mediaUploader.pushMedia(uploadUrl, request)
                if (response.isSuccessful) {
                    if (kind == "link") {
                        response.body()?.location!!
                    } else {
                        args.asset.assetId
                    }
                } else {
                    throw Exception("Failed to upload media")
                }
            }
        } else {
            return emptyList()
        }
    }

    suspend fun submit(context: Context): Result<Unit> {
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
                kind = if (state.kind == Kind.Gallery) "gallery" else "link"
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
                api.submitGalleryPost(galleryFinal)
            } else {
                api.submitPost(submission.getOrThrow())
            }
            loading = false
            return if (res.isSuccessful) {
                val response = res.body()
                Log.d("PostCreatorViewModel", "submit: $response")
                if (response?.json?.errors?.isNotEmpty() == true) {
                    Result.failure(Exception(response.json.errors.toString()))
                } else {
                    Result.success(Unit)
                }
            } else {
                Result.failure(Exception("Failed to submit post: ${res.errorBody()}"))
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