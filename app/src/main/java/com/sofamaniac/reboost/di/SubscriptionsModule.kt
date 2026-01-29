package com.sofamaniac.reboost.di

import com.sofamaniac.reboost.data.remote.api.RedditAPIService
import com.sofamaniac.reboost.domain.repository.AccountsRepository
import com.sofamaniac.reboost.domain.repository.SubscriptionsRepository
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
