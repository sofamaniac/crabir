package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.domain.repository.CommunityViewRepository
import com.sofamaniac.crabir.domain.repository.LinksRepository
import com.sofamaniac.crabir.domain.repository.LinksRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import com.sofamaniac.crabir.domain.repository.RoomRepository as RoomCommunityViewRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLinksRepository(
        linksRepositoryImpl: LinksRepositoryImpl
    ): LinksRepository

    @Binds
    abstract fun bindCommunityViewRepository(communityRepository: RoomCommunityViewRepository): CommunityViewRepository
}