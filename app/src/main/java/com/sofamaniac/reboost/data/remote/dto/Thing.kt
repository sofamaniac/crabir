/*
 * *
 *  * Created by sofamaniac
 *  * Copyright (c) 2026 . All rights reserved.
 *  * Last modified 1/12/26, 11:40 PM
 *
 */

package com.sofamaniac.reboost.data.remote.dto

import com.sofamaniac.reboost.data.remote.dto.comment.CommentDTO
import com.sofamaniac.reboost.data.remote.dto.post.PostDTO
import com.sofamaniac.reboost.data.remote.dto.subreddit.SubredditData
import com.sofamaniac.reboost.data.remote.dto.subreddit.dummySubredditData
import com.sofamaniac.reboost.reddit.ListingData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
sealed class Thing {


    @Serializable
    @SerialName("t1")
    data class Comment(val data: CommentDTO) : Thing()

    @Serializable
    @SerialName("t2")
    data class User(val data: String) : Thing()

    @Serializable
    @SerialName("t3")
    data class Post(val data: PostDTO) : Thing()

    @Serializable
    @SerialName("t5")
    data class Subreddit(val data: SubredditData = dummySubredditData()) :
        Thing()

    @Serializable
    @SerialName("Listing")
    data class Listing<T>(
        val data: ListingData<T>
    ) : Thing(), Iterable<T> {

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
    data class More(val data: MoreData) : Thing()

    @Serializable
    @SerialName("LabeledMulti")
    data class Multi(val data: MultiData) : Thing()

}

fun emptyListing(): Thing.Listing<Thing> {
    return Thing.Listing(data = ListingData())
}

@Serializable
data class MoreData(
    val count: Int,
    val name: String,
    val id: String,
    val parent_id: String,
    val depth: Int,
    val children: List<String>,
)