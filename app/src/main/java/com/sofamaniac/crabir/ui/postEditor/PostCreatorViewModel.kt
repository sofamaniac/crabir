package com.sofamaniac.crabir.ui.postEditor

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
import com.sofamaniac.crabir.data.remote.api.GalleryItem
import com.sofamaniac.crabir.data.remote.api.MediaUploadInterface
import com.sofamaniac.crabir.data.remote.api.PostSubmissionBuilder
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.data.remote.api.Rules
import com.sofamaniac.crabir.data.remote.api.SubmissionBuilderError
import com.sofamaniac.crabir.data.remote.api.makeMediaUploadBody
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.SubredditData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostCreatorViewModel @Inject constructor(
    private val api: RedditAPIService,
    private val mediaUploader: MediaUploadInterface
) :
    ViewModel() {
    var state by mutableStateOf(PostSubmissionBuilder())
    var community: SubredditData? by mutableStateOf(null)

    var rules: Rules by mutableStateOf(Rules())
    var flairs: List<String> by mutableStateOf(emptyList())

    var error: SubmissionBuilderError? by mutableStateOf(null)

    val titleState = TextFieldState()
    val textState = TextFieldState()
    val urlState = TextFieldState()

    var media: List<Uri> by mutableStateOf(emptyList())
    var captions: MutableMap<Uri, String> = mutableMapOf()
    var loading by mutableStateOf(false)

    fun getRules() {
        if (rules.siteRules.isNotEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            val res = api.getRules(community!!.displayName)
            if (res.isSuccessful) {
                rules = res.body()!!
            }
        }
    }

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

    fun submit(context: Context, onSucceed: () -> Unit) {
        loading = true
        state = state.copy(
            title = titleState.text as String,
            text = textState.text as String,
            url = urlState.text as String,
            subreddit = community?.displayName ?: ""
        )
        setKind(context)
        viewModelScope.launch(Dispatchers.IO) {
            var submission = state.build()
            if (submission.isFailure) {
                Log.e("PostCreatorViewModel", "submit: ${submission.exceptionOrNull()}")
                error = submission.exceptionOrNull() as SubmissionBuilderError?
                loading = false
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
                if (res.isSuccessful) {
                    onSucceed()
                }
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