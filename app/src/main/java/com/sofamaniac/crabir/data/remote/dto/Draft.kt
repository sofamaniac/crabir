package com.sofamaniac.crabir.data.remote.dto

import com.sofamaniac.crabir.data.remote.utils.DraftBodySerializer
import com.sofamaniac.crabir.data.remote.utils.InstantAsLongSerializer
import com.sofamaniac.crabir.domain.model.Flair
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.RichtextDocument
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class Draft(
    val subreddit: Fullname,
    val kind: String,
    val title: String,
    @SerialName("'original_content")
    val originalContent: Boolean = false,
    val flair: Flair? = null,
    @SerialName("send_replies")
    val sendReplies: Boolean,
    val id: String,
    @Serializable(with = DraftBodySerializer::class)
    val body: DraftBody?,
    @Serializable(with = InstantAsLongSerializer::class)
    val created: Instant,
)

@Serializable
sealed class DraftBody {
    @Serializable
    @SerialName("text")
    data class Text(val text: String) : DraftBody()

    @Serializable
    @SerialName("richtext")
    data class Richtext(val richtext: RichtextDocument) : DraftBody()
}

