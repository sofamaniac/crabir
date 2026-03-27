package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.domain.repository.ThreadRepository
import com.sofamaniac.crabir.domain.repository.ThreadRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ThreadModule {

//    @Provides
//    fun provideThreadRepository(
//        api: RedditAPIService,
//        visitedPostsDao: VisitedPostsDao,
//        votableRepository: VotableRepository
//    ): ThreadRepository {
//        return ThreadRepositoryImpl(api, visitedPostsDao, votableRepository)
//
//    }

}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ThreadModuleInterface {
    @Binds
    abstract fun bindsThreadRepository(
        threadRepository: ThreadRepositoryImpl
    ): ThreadRepository
}
