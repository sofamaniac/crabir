package com.sofamaniac.reboost.di

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.repository.PostRepository
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
    ): PostRepository {
        return PostRepository(api)

    }
}
