/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 11:40 PM
 *
 */

package com.sofamaniac.crabir.data.remote.dto

import com.sofamaniac.crabir.data.remote.dto.comment.CommentDTO
import com.sofamaniac.crabir.data.remote.dto.post.PostDTO
import com.sofamaniac.crabir.data.remote.dto.subreddit.SubredditDTO
import com.sofamaniac.crabir.data.remote.dto.subreddit.dummySubredditData
import com.sofamaniac.crabir.data.remote.dto.user.UserDTO
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.repository.DataInterface
import com.sofamaniac.crabir.reddit.ListingData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
sealed class Thing : DataInterface {

    @Serializable
    @SerialName("t1")
    data class Comment(val data: CommentDTO) : Thing() {
        override val id: String = data.id
        override val name: Fullname = data.name
    }

    @Serializable
    @SerialName("t2")
    data class User(val data: UserDTO) : Thing() {
        @OptIn(ExperimentalUuidApi::class)
        override val id: String
            get() = data.id.ifBlank { Uuid.random().toString() }
        override val name: Fullname = data.name
    }

    @Serializable
    @SerialName("t3")
    data class Post(val data: PostDTO) : Thing() {
        override val id: String = data.id
        override val name: Fullname = data.fullname
    }

    @Serializable
    @SerialName("t5")
    data class Subreddit(val data: SubredditDTO = dummySubredditData()) :
        Thing() {
        override val id: String = data.id
        override val name: Fullname = data.name
    }

    @Serializable
    @SerialName("Listing")
    data class Listing<T>(
        val data: ListingData<T>
    ) : Thing(), Iterable<T> {

        override val id: String = "Listing"
        override val name: Fullname = Fullname("Listing")

        val size: Int get() = data.children.size

        override fun iterator(): Iterator<T> {
            return data.children.iterator()
        }

        fun isEmpty(): Boolean {
            return data.children.isEmpty()
        }
    }

    @Serializable
    @SerialName("more")
    data class More(val data: MoreData) : Thing() {
        override val id: String = data.id
        override val name: Fullname = data.name
    }

    @Serializable
    @SerialName("LabeledMulti")
    data class Multi(val data: MultiData) : Thing() {
        override val id: String = data.name.name
        override val name: Fullname = data.name
    }

}

fun emptyListing(): Thing.Listing<Thing> {
    return Thing.Listing(data = ListingData())
}

@Serializable
data class MoreData(
    val count: Int,
    val name: Fullname,
    val id: String,
    @SerialName("parent_id") val parentId: Fullname,
    val depth: Int,
    val children: List<String>,
)