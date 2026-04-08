package com.sofamaniac.crabir.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.toEntity
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PostDataViewModel.Factory::class)
class PostDataViewModel @AssistedInject constructor(
    private val repository: LinksRepository,
    private val visitedPostsDao: VisitedPostsDao,
    @Assisted name: String,
) : ViewModel() {
    val fullname = Fullname(name)
    val post = repository.get(fullname)

    @AssistedFactory
    interface Factory {
        fun create(name: String): PostDataViewModel
    }

    fun visitPost(post: PostData) {
        viewModelScope.launch(Dispatchers.IO) {
            visitedPostsDao.insert(post.toEntity(timestamp = System.currentTimeMillis()))
        }
    }
}