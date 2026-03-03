package com.sofamaniac.reboost.di

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.VotableRepository
import com.sofamaniac.reboost.domain.repository.feed.HomeRepository
import com.sofamaniac.reboost.domain.repository.feed.SavedRepository
import com.sofamaniac.reboost.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.reboost.domain.repository.search.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideHomeRepository(
        votableRepository: VotableRepository,
        api: RedditAPIService
    ): HomeRepository {
        return HomeRepository(votableRepository, api)
    }

    @Provides
    fun provideSubredditPostsRepository(
        votableRepository: VotableRepository,
        api: RedditAPIService
    ): SubredditPostsRepository {
        return SubredditPostsRepository(votableRepository, api)
    }

    @Provides
    fun provideSavedRepository(
        votableRepository: VotableRepository,
        api: RedditAPIService,
        accountsRepository: AccountsRepository
    ): SavedRepository {
        return SavedRepository(votableRepository, api, accountsRepository)
    }

    @Provides
    fun provideSearchPostRepository(
        api: RedditAPIService
    ): SearchRepository {
        return SearchRepository(api = api)
    }

}

