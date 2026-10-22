package com.sofamaniac.crabir.ui.votable

import com.sofamaniac.crabir.domain.model.Fullname


sealed interface VotableEvent : CommentEvent, PostEvent {
    data class Upvote(val name: Fullname) : VotableEvent
    data class Downvote(val name: Fullname) : VotableEvent
    data class Save(val name: Fullname, val target: Boolean, val upvote: Boolean) : VotableEvent
    data class Reply(val name: Fullname) : VotableEvent

    // Dialogs
    data class ShowShare(val name: Fullname) : VotableEvent
    data class ShowUser(val username: String) : VotableEvent
    data class ShowEdit(val name: Fullname) : VotableEvent
    data class ShowReport(val name: Fullname) : VotableEvent
}


sealed interface CommentEvent {
    data class ShowCopy(val name: Fullname) : CommentEvent
    data class ShowBar(val name: Fullname?) : CommentEvent
    data class Collapse(val name: Fullname, val target: Boolean? = null) : CommentEvent
}

sealed interface PostEvent {
    data class ShowMute(val name: Fullname) : PostEvent
}

