package com.sofamaniac.crabir.ui.inbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.utils.fromHtml
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.Message
import com.sofamaniac.crabir.domain.model.RichtextDocument
import com.sofamaniac.crabir.domain.repository.InboxRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MessageViewModel(
    private val messageRepository: InboxRepository,
    @InjectedParam val name: Fullname,
    @InjectedParam initialMessage: Message,
) : ViewModel() {
    val message: StateFlow<Message> = messageRepository.get(name).map {
        Log.d("MessageViewModel", "message: $it")
        it ?: initialMessage
    }
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = initialMessage)

    val richtext: StateFlow<RichtextDocument> =
        message.map { RichtextDocument.fromHtml(it.bodyHtml ?: "") }
            .stateIn(
                viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = RichtextDocument.fromHtml(initialMessage.bodyHtml ?: "")
            )

    fun markRead() {
        viewModelScope.launch {
            messageRepository.read(name).onFailure {
                Log.e("MessageViewModel", "markRead: $it")
            }
        }
    }

    fun markUnread() {
        viewModelScope.launch {
            messageRepository.unread(name)
        }
    }
}