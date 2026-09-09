/*
 * Copyright (c) 2025 Antoine Grimod
 */

package com.sofamaniac.crabir.data.remote.reddit

import android.content.Context
import android.net.Uri
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Kind
import com.sofamaniac.crabir.domain.model.RedditAccount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Tag
import retrofit2.http.Url

interface PostAPI {

    @POST("/api/hide")
    suspend fun hide(@Query("id") postFullname: Fullname): Result<Unit>

    @POST("/api/unhide")
    suspend fun unhide(@Query("id") postFullname: Fullname): Result<Unit>

    @FormUrlEncoded
    @POST("api/submit")
    suspend fun submitPost(
        @FieldMap post: Map<String, String>,
        @Tag account: RedditAccount?,
    ): Result<PostResponse>

    @POST("api/submit_gallery_post.json")
    suspend fun submitGalleryPost(
        @Body body: GallerySubmission,
        @Tag account: RedditAccount?,
    ): Result<PostResponse>

    @FormUrlEncoded
    @POST("api/media/asset.json")
    suspend fun uploadMedia(
        @Field("filepath") filepath: String,
        @Field("mimetype") mimetype: String,
        @Tag account: RedditAccount?,
    ): Result<MediaUploadResponse>

    @FormUrlEncoded
    @POST("api/spoiler")
    /** Mark post as spoiler */
    suspend fun spoiler(@Field("id") postFullname: Fullname): Result<Unit>

    @FormUrlEncoded
    @POST("api/unspoiler")
    /** Unmark post as spoiler */
    suspend fun unspoiler(@Field("id") postFullname: Fullname): Result<Unit>

    @FormUrlEncoded
    @POST("api/marknsfw")
    suspend fun markNSFW(@Field("id") postFullname: Fullname): Result<Unit>

    @FormUrlEncoded
    @POST("api/unmarknsfw")
    suspend fun unmarkNSFW(@Field("id") postFullname: Fullname): Result<Unit>

    @FormUrlEncoded
    @POST("api/editusertext")
    /** Edit the body text of a comment or self post */
    suspend fun editUserText(
        @Field("thing_id") name: Fullname,
        @Field("text") text: String,
        @Field("api_type") apiType: String = "json",
        @Field("video_poster_url") videoPosterUrl: String? = null,
    ): Result<Unit>

    @FormUrlEncoded
    @POST("api/sendreplies")
    suspend fun setSendReplies(
        @Field("id") postFullname: Fullname,
        @Field("state") sendReplies: Boolean,
    ): Result<Unit>

    @FormUrlEncoded
    @POST("api/del")
    suspend fun delete(@Field("id") name: Fullname): Result<Unit>

    @FormUrlEncoded
    @POST("{subreddit}/api/selectflair")
    suspend fun selectFlair(
        @Path("subreddit", encoded = true) subreddit: String,
        @Field("link") postFullname: Fullname,
        @Field("flair_template_id") flairId: String,
        @Field("text") text: String?,
    ): Result<Unit>

    @FormUrlEncoded
    @POST("{subreddit}/api/selectflair")
    suspend fun getFlairs(
        @Path("subreddit", encoded = true) subreddit: String,
        @Field("link") postFullname: Fullname,
    ): Result<List<FlairInfo>>
}

@Serializable
data class PostResponse(
    val json: PostResponseInner,
)

@Serializable
data class PostResponseInner(
    val errors: List<List<String>> = emptyList(),
)

interface MediaUploadInterface {
    @POST
    suspend fun pushMedia(@Url url: String, @Body body: RequestBody): Result<MediaPushResponse>
}

@Serializable
@XmlSerialName("PostResponse")
data class MediaPushResponse(
    @XmlSerialName("Location") @XmlElement(true) val location: String,
    @XmlSerialName("Bucket") @XmlElement(true) val bucket: String,
    @XmlSerialName("Key") @XmlElement(true) val key: String,
    @XmlSerialName("ETag") @XmlElement(true) val etag: String,
)

@Serializable
data class MediaUploadResponse(
    val args: Args,
    val asset: Asset,
)

@Serializable
data class Asset(
    @SerialName("asset_id") val assetId: String,
)

@Serializable
data class Args(
    val action: String,
    val fields: List<MediaField>,
)

@Serializable
data class MediaField(
    val name: String,
    val value: String,
)

sealed class SubmissionBuilderError(message: String, cause: Throwable? = null) :
    Exception(message, cause)

class MissingTitle(cause: Throwable? = null) : SubmissionBuilderError("Missing title", cause)
class MissingCommunity(cause: Throwable? = null) :
    SubmissionBuilderError("Missing community", cause)

class MissingUrl(cause: Throwable? = null) : SubmissionBuilderError("Missing url", cause)
class InvalidUrl(cause: Throwable? = null) : SubmissionBuilderError("Invalid url", cause)
class MissingText(cause: Throwable? = null) : SubmissionBuilderError("Missing text", cause)
class MissingGallery(cause: Throwable? = null) : SubmissionBuilderError("Missing gallery", cause)

@Serializable
data class GalleryItem(
    val caption: String = "",
    @SerialName("outbound_url") val outboundUrl: String = "",
    @SerialName("media_id") val mediaId: String,
)

@Serializable
data class GallerySubmission(
    val title: String = "",
    @SerialName("sr") val subreddit: String = "",
    val nsfw: Boolean = false,
    val spoiler: Boolean = false,
    @SerialName("sendreplies") val sendReplies: Boolean = false,
    @SerialName("flair_id") val flairId: String? = null,
    @SerialName("flair_text") val flairText: String? = null,
    val items: List<GalleryItem> = emptyList(),
    @SerialName("api_type") val apiType: String = "json",
    @SerialName("show_error_list") val showErrorList: Boolean = true,
    @SerialName("validate_on_submit") val validateOnSubmit: Boolean = true,
)

@Serializable
data class PostSubmissionBuilder(
    val title: String = "",
    val text: String? = null,
    @SerialName("sr") val subreddit: String = "",
    val nsfw: Boolean = false,
    val spoiler: Boolean = false,
    @SerialName("sendreplies") val sendReplies: Boolean = false,
    val flairId: String? = null,
    val flairText: String? = null,
    val url: String? = null,
    val kind: Kind = Kind.Self,
) {

    fun toGallerySubmission(): GallerySubmission {
        return GallerySubmission(
            title = title,
            subreddit = subreddit,
            nsfw = nsfw,
            spoiler = spoiler,
            sendReplies = sendReplies,
            flairId = flairId,
            flairText = flairText,
        )
    }

    private fun check(): Result<Unit> {
        return when {
            title.isBlank() -> Result.failure(MissingTitle())
            subreddit.isBlank() -> Result.failure(MissingCommunity())
            kind == Kind.Link && url.isNullOrBlank() -> Result.failure(MissingUrl())
            kind == Kind.Self && text.isNullOrBlank() -> Result.failure(MissingText())
            kind == Kind.Link && url?.runCatching { toHttpUrl() }?.isSuccess != true -> Result.failure(
                InvalidUrl()
            )

            else -> Result.success(Unit)
        }
    }

    fun build(): Result<Map<String, String>> {
        val result = check()
        return if (result.isFailure) {
            Result.failure(result.exceptionOrNull()!!)
        } else {
            Result.success(
                buildMap {
                    put("api_type", "json")
                    kind.toApiString()?.let {
                        put("kind", it)
                    }
                    put("title", title)
                    put("sr", subreddit)
                    put("sendreplies", sendReplies.toString())
                    put("nsfw", nsfw.toString())
                    put("spoiler", spoiler.toString())
                    put("show_error_list", true.toString())
                    put("validate_on_submit", true.toString())
                    if (!text.isNullOrBlank()) {
                        put("text", text)
                    }
                    if (!url.isNullOrBlank()) {
                        put("url", url)
                        if (kind == Kind.Video) {
                            put("video_poster_url", url)
                        }
                    }
                    if (flairId != null) {
                        put("flair_id", flairId)
                    }
                    if (flairText != null) {
                        put("flair_text", flairText)
                    }
                }
            )
        }
    }
}

@Serializable
data class CrosspostSubmissionBuilder(
    val crosspostFullname: Fullname,
    val title: String = "",
    @SerialName("sr") val subreddit: String = "",
    val nsfw: Boolean = false,
    val spoiler: Boolean = false,
    @SerialName("sendreplies") val sendReplies: Boolean = false,
    val flairId: String? = null,
    val flairText: String? = null,
) {

    fun build(): Result<Map<String, String>> {
        return if (title.isBlank()) {
            Result.failure(MissingTitle())
        } else if (subreddit.isBlank()) {
            Result.failure(MissingCommunity())
        } else {
            Result.success(
                buildMap {
                    put("api_type", "json")
                    put("kind", "crosspost")
                    put("title", title)
                    put("sr", subreddit)
                    put("sendreplies", sendReplies.toString())
                    put("nsfw", nsfw.toString())
                    put("spoiler", spoiler.toString())
                    put("show_error_list", true.toString())
                    put("validate_on_submit", true.toString())
                    put("crosspost_fullname", crosspostFullname.name)
                    if (flairId != null) {
                        put("flair_id", flairId)
                    }
                    if (flairText != null) {
                        put("flair_text", flairText)
                    }
                }
            )
        }
    }
}

internal fun Kind.toApiString(): String? {
    return when (this) {
        Kind.Self -> "self"
        Kind.Image -> "image"
        Kind.Video -> "video"
        Kind.Link -> "link"
        Kind.Gallery -> null
        else -> "link"
    }
}

fun makeMediaUploadBody(context: Context, uri: Uri, data: List<MediaField>): RequestBody {
    val file = context.contentResolver.openInputStream(uri)?.use {
        it.buffered().readBytes()
    }!!
    return MultipartBody.Builder()
        .apply {
            // Must come before the file because amazon stop reading form after the file
            var builder = this
            data.forEach { field ->
                builder = builder.addFormDataPart(field.name, field.value)
            }
        }
        .addFormDataPart(
            name = "file",
            filename = uri.toString(),
            body = file.toRequestBody()
        )
        .build()
}
