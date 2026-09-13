package com.sofamaniac.crabir.ui.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.remote.dto.Draft
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.SubredditData
import com.sofamaniac.crabir.domain.repository.DraftsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DraftsViewModel(private val repository: DraftsRepository) : ViewModel() {

    val drafts: StateFlow<List<Draft>> = repository.drafts
    val subreddits: StateFlow<Map<Fullname, SubredditData>> = repository.subreddits

    var refreshing: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            repository.getDrafts()
        }
    }

    fun refresh() {
        refreshing.value = true
        viewModelScope.launch {
            repository.getDrafts()
        }.invokeOnCompletion { refreshing.value = false }
    }

    fun delete(draft: Draft) {
        viewModelScope.launch {
            repository.deleteDraft(draft.id)
        }
    }
}
