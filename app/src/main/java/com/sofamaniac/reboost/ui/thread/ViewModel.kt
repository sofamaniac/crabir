package com.sofamaniac.reboost.ui.thread

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.reboost.data.local.dao.VisitedPostsDao
import com.sofamaniac.reboost.data.local.entities.toDomainModel
import com.sofamaniac.reboost.data.remote.dto.Thing
import com.sofamaniac.reboost.data.remote.dto.comment.Sort
import com.sofamaniac.reboost.domain.model.PostData
import com.sofamaniac.reboost.domain.repository.ThreadRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel(assistedFactory = ThreadViewModel.Factory::class)
class ThreadViewModel @AssistedInject constructor(
    private val repository: ThreadRepository,
    private val visitedPostsDao: VisitedPostsDao,
    @Assisted val permalink: String,
): ViewModel() {

    var id: String = repository.getPostId(permalink)

    val isRefreshing: StateFlow<Boolean>
        get() = repository.isRefreshing


    private var _comments = MutableStateFlow<List<Thing>>(emptyList())
    val comments: StateFlow<List<Thing>> = _comments.asStateFlow()


    private val _sort = MutableStateFlow(Sort.Best)
    val sort: StateFlow<Sort> = _sort.asStateFlow()

    init {
        fetchComments()
    }

    fun getPost(): PostData {
        val post = runBlocking(Dispatchers.IO) {
            val post = repository.getPost(id) ?: visitedPostsDao.getPost(id)?.toDomainModel()
            if (post == null) {
                Log.e("ThreadViewModel", "getPost: Post not found in database ($id)")
            }
            post!!
        }
        return post
    }

    fun fetchComments() {
        viewModelScope.launch {
            _comments.value = repository.getComments(permalink, sort = _sort.value)
        }
    }

    fun getComments(): List<Thing> {
        val comments = runBlocking(Dispatchers.IO) {
            repository.getComments(permalink, sort = _sort.value)
        }
        return comments
    }

    fun setSort(sort: Sort) {
        _sort.value = sort
    }


    fun refresh() {
        repository.refresh()
    }

    @AssistedFactory
    interface Factory {
        fun create(permalink: String): ThreadViewModel
    }
}