package com.sofamaniac.crabir.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.model.PostData
import com.sofamaniac.crabir.domain.repository.LinksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class PostDataViewModel(
    private val repository: LinksRepository,
    private val visitedPostsDao: VisitedPostsDao,
    @InjectedParam val name: Fullname,
) : ViewModel() {
    val post = repository.get(name)

    fun visitPost(post: PostData, visitedBy: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val entity = VisitedPostEntity(post.name, System.currentTimeMillis(), visitedBy)
            visitedPostsDao.insert(entity)
        }
    }
}
