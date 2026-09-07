package com.sofamaniac.crabir.di

import android.content.Context
import androidx.room.Room
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.dao.CommunityDao
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.InboxDao
import com.sofamaniac.crabir.data.local.dao.MultiDao
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.database.AccountDatabase
import com.sofamaniac.crabir.data.local.database.CommunityViewDatabase
import com.sofamaniac.crabir.data.local.database.InboxDatabase
import com.sofamaniac.crabir.data.local.database.MultiDatabase
import com.sofamaniac.crabir.data.local.database.SubredditDatabase
import com.sofamaniac.crabir.data.local.database.VisitedPostsDatabase
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.model.SubredditData
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@Configuration
object DatabaseModule {

    @Singleton
    fun provideAccountDatabase(context: Context): AccountDatabase {
        return Room.databaseBuilder(
            context,
            AccountDatabase::class.java,
            "reddit_account_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    fun provideVisitedPostsDatabase(context: Context): VisitedPostsDatabase {
        return Room.databaseBuilder(
            context,
            VisitedPostsDatabase::class.java,
            "visited_posts_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    fun provideCommunityViewDatabase(context: Context): CommunityViewDatabase {
        return Room.databaseBuilder(
            context,
            CommunityViewDatabase::class.java,
            "community_view_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    fun provideSubredditDatabase(context: Context): SubredditDatabase {
        return Room.databaseBuilder(
            context,
            SubredditDatabase::class.java,
            "subreddit_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    fun provideMultiDatabase(context: Context): MultiDatabase {
        return Room.databaseBuilder(
            context,
            MultiDatabase::class.java,
            "multi_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Singleton
    fun provideInboxDatabase(context: Context): InboxDatabase {
        return Room.databaseBuilder(
            context,
            InboxDatabase::class.java,
            "inbox_database"
        ).fallbackToDestructiveMigration(false)
            .build()
    }


    @Singleton
    fun provideAccountsDao(database: AccountDatabase): AccountsDao {
        return database.accountsDao()
    }

    @Singleton
    fun provideVisitedPostsDao(database: VisitedPostsDatabase): VisitedPostsDao {
        return database.visitedPostsDao()
    }

    @Singleton
    fun provideVisitedCommunitiesDao(database: CommunityViewDatabase): CommunityViewDao {
        return database.visitedCommunityDao()
    }

    @Singleton
    fun provideVotableDao(database: VisitedPostsDatabase): VotableDao {
        return database.votableDao()
    }

    @Singleton(binds = [SubredditDao::class])
    fun subredditDao(database: SubredditDatabase): CommunityDao<SubredditData> {
        return database.subredditDao()
    }

    @Singleton(binds = [MultiDao::class])
    fun multiDao(database: MultiDatabase): CommunityDao<MultiData> {
        return database.multiDao()
    }

    @Singleton
    fun inboxDao(database: InboxDatabase): InboxDao {
        return database.inboxDao()
    }

}
