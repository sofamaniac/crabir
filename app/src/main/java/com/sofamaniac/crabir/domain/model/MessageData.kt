package com.sofamaniac.crabir.domain.model

import com.sofamaniac.crabir.data.remote.dto.MessageDTO
import com.sofamaniac.crabir.data.remote.dto.comment.CommentDTO
import com.sofamaniac.crabir.domain.repository.DataInterface

sealed class MessageData : DataInterface {
    data class Message(val message: MessageDTO) : MessageData() {
        override val id: String = message.id
        override val name: Fullname = message.name
    }

    data class Comment(val comment: CommentDTO) : MessageData() {
        override val id: String = comment.id
        override val name: Fullname = comment.name
    }
}