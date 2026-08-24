package com.sofamaniac.crabir.ui.post

import androidx.lifecycle.ViewModel
import com.sofamaniac.crabir.domain.model.Fullname
import com.sofamaniac.crabir.domain.repository.LinksRepository
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class PostDataViewModel(
    repository: LinksRepository,
    @InjectedParam val name: Fullname,
) : ViewModel() {
    val post = repository.get(name)
}
