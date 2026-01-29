package com.sofamaniac.reboost.di

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.PostRepository
import com.sofamaniac.reboost.domain.repository.feed.HomeRepository
import com.sofamaniac.reboost.domain.repository.feed.SavedRepository
import com.sofamaniac.reboost.domain.repository.feed.SubredditPostsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideHomeRepository(
        postRepository: PostRepository,
        api: RedditAPIService
    ): HomeRepository {
        return HomeRepository(postRepository, api)
    }

    @Provides
    fun provideSubredditPostsRepository(
        postRepository: PostRepository,
        api: RedditAPIService
    ): SubredditPostsRepository {
        return SubredditPostsRepository(postRepository, api)
    }

    @Provides
    @Singleton
    fun provideSavedRepository(
        postRepository: PostRepository,
        api: RedditAPIService,
        accountsRepository: AccountsRepository
    ): SavedRepository {
        return SavedRepository(postRepository, api, accountsRepository)
    }
}

