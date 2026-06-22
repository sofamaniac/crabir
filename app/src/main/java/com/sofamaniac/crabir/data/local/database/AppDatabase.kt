package com.sofamaniac.crabir.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.MultiDao
import com.sofamaniac.crabir.data.local.dao.SubredditDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.local.entities.RedditAccountEntity
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.model.SubredditData


@Database(entities = [RedditAccountEntity::class], version = 2)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
}

@Database(
    entities = [
        VisitedPostEntity::class,
        CommunityViewEntity::class,
        VotableEntity::class,
        SubredditData::class,
        MultiData::class
    ],
    version = 13
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun visitedPostsDao(): VisitedPostsDao
    abstract fun visitedCommunityDao(): CommunityViewDao
    abstract fun votableDao(): VotableDao

    abstract fun subredditDao(): SubredditDao

    abstract fun multiDao(): MultiDao
}
