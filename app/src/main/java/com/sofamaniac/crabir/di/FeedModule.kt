package com.sofamaniac.crabir.di

import com.sofamaniac.crabir.data.remote.api.RedditAPIService
import com.sofamaniac.crabir.domain.repository.VotableRepository
import com.sofamaniac.crabir.domain.repository.feed.HomeRepository
import com.sofamaniac.crabir.domain.repository.feed.SubredditPostsRepository
import com.sofamaniac.crabir.domain.repository.search.CommentSearchRepository
import com.sofamaniac.crabir.domain.repository.search.CommunitySearchRepository
import com.sofamaniac.crabir.domain.repository.search.PostSearchRepository
import com.sofamaniac.crabir.domain.repository.search.UserSearchRepository
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
    fun provideSearchPostRepository(
        api: RedditAPIService
    ): PostSearchRepository {
        return PostSearchRepository(api = api)
    }

    @Provides
    fun provideSearchCommunityRepository(
        api: RedditAPIService
    ): CommunitySearchRepository {
        return CommunitySearchRepository(api = api)
    }

    @Provides
    fun provideSearchUserRepository(api: RedditAPIService): UserSearchRepository {
        return UserSearchRepository(api = api)
    }

    @Provides
    fun provideSearchCommentRepository(api: RedditAPIService): CommentSearchRepository {
        return CommentSearchRepository(api = api)
    }

}

