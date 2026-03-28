package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.repository.VotableRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostsModule {
    @Provides
    @Singleton
    fun providesPostsRepository(
        api: RedditAPIService,
        votableDao: VotableDao,
    ): VotableRepository {
        return VotableRepository(api, votableDao)
    }
}
