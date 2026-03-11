package com.sofamaniac.crabir.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sofamaniac.crabir.data.local.dao.AccountsDao
import com.sofamaniac.crabir.data.local.dao.VisitedCommunityDao
import com.sofamaniac.crabir.data.local.dao.VisitedPostsDao
import com.sofamaniac.crabir.data.local.entities.RedditAccountEntity
import com.sofamaniac.crabir.data.local.entities.VisitedCommunityEntity
import com.sofamaniac.crabir.data.local.entities.VisitedPostEntity

@Database(
    entities = [RedditAccountEntity::class, VisitedPostEntity::class, VisitedCommunityEntity::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
    abstract fun visitedPostsDao(): VisitedPostsDao

    abstract fun visitedCommunityDao(): VisitedCommunityDao
}