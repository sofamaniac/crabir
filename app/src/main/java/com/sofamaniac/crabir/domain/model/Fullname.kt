package com.sofamaniac.crabir.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
@Parcelize
value class Fullname(val name: String) : Parcelable {
    val kind: FullnameKind
        get() {
            return when (name.split("_")[0]) {
                "t1" -> FullnameKind.Comment
                "t2" -> FullnameKind.User
                "t3" -> FullnameKind.Post
                "t4" -> FullnameKind.Message
                "t5" -> FullnameKind.Subreddit
                else -> FullnameKind.Multi
            }
        }
}

enum class FullnameKind {
    Post,
    Comment,
    User,
    Subreddit,
    Message,
    Multi,
}
