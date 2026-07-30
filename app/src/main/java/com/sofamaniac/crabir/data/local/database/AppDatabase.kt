package com.sofamaniac.crabir.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.dao.CommunityViewDao
import com.sofamaniac.crabir.data.local.dao.InboxDao
import com.sofamaniac.crabir.data.local.dao.MultiRepository
import com.sofamaniac.crabir.data.local.dao.SubredditRepository
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.dao.VotableDao
import com.sofamaniac.crabir.data.local.entities.CommunityViewEntity
import com.sofamaniac.crabir.data.local.entities.RedditAccountEntity
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity
import com.sofamaniac.crabir.data.local.entities.VotableEntity
import com.sofamaniac.crabir.data.remote.dto.MultiData
import com.sofamaniac.crabir.domain.model.Message
import com.sofamaniac.crabir.domain.model.SubredditData


@Database(entities = [RedditAccountEntity::class], version = 2)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
}

@Database(entities = [VisitedPostEntity::class, VotableEntity::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class VisitedPostsDatabase : RoomDatabase() {
    abstract fun visitedPostsDao(): VisitedPostsDao
    abstract fun votableDao(): VotableDao
}

@Database(entities = [CommunityViewEntity::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class CommunityViewDatabase : RoomDatabase() {
    abstract fun visitedCommunityDao(): CommunityViewDao
}

@Database(entities = [SubredditData::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class SubredditDatabase : RoomDatabase() {
    abstract fun subredditDao(): SubredditRepository
}

@Database(entities = [MultiData::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class MultiDatabase : RoomDatabase() {
    abstract fun multiDao(): MultiRepository
}

@Database(entities = [Message::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class InboxDatabase : RoomDatabase() {
    abstract fun inboxDao(): InboxDao
}