package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.repository.ThreadRepository
import com.sofamaniac.crabir.domain.repository.ThreadRepositoryImpl
import com.sofamaniac.crabir.domain.repository.VotableRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ThreadModule {

    @Provides
    fun provideThreadRepository(
        api: RedditAPIService,
        visitedPostsDao: VisitedPostsDao,
        votableRepository: VotableRepository
    ): ThreadRepository {
        return ThreadRepositoryImpl(api, visitedPostsDao, votableRepository)
    }

}