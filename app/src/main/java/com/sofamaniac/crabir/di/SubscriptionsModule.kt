package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.repository.AccountsRepository
import com.sofamaniac.crabir.domain.repository.SubscriptionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SubscriptionsModule {

    @Singleton
    @Provides
    fun provideSubscriptionsRepository(
        api: RedditAPIService,
        accountsRepository: AccountsRepository
    ): SubscriptionsRepository {
        return SubscriptionsRepository(api, accountsRepository)
    }

}
