package com.sofamaniac.crabir.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
//    @Provides
//    fun provideHomeRepository(
//        votableRepository: VotableRepository,
//        api: RedditAPIService
//    ): HomeRepository {
//        return HomeRepository(votableRepository, api)
//    }

//    @Provides
//    fun provideHistoryRepository(
//        visitedPostsDao: VisitedPostsDao,
//        votableRepository: VotableRepository,
//        api: RedditAPIService
//    ): HistoryRepository {
//        return HistoryRepository(visitedPostsDao, votableRepository, api)
//    }

//    @Provides
//    fun provideSubredditPostsRepository(
//        votableRepository: VotableRepository,
//        api: RedditAPIService
//    ): SubredditPostsRepository {
//        return SubredditPostsRepository(votableRepository, api)
//    }

    //    @Provides
//    fun provideSearchPostRepository(
//        api: RedditAPIService,
//        votableRepository: VotableRepository,
//    ): PostSearchRepository {
//        return PostSearchRepository(api = api, votableRepository = votableRepository)
//    }
//
//    @Provides
//    fun provideSearchCommunityRepository(
//        api: RedditAPIService
//    ): CommunitySearchRepository {
//        return CommunitySearchRepository(api = api)
//    }
//
//    @Provides
//    fun provideSearchUserRepository(api: RedditAPIService): UserSearchRepository {
//        return UserSearchRepository(api = api)
//    }

//    @Provides
//    fun provideSearchCommentRepository(api: RedditAPIService): CommentSearchRepository {
//        return CommentSearchRepository(api = api)
//    }

}

