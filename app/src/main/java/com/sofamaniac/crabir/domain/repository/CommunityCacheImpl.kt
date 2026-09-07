package com.sofamaniac.crabir.domain.repository

import android.util.Log
import com.sofamaniac.crabir.data.local.dao.CommunityDao
import com.sofamaniac.crabir.domain.model.CommunityData
import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Singleton

@Singleton
abstract class CommunityCache<T : CommunityData>(val dao: CommunityDao<T>) : Cache<T> {
    override fun insert(thing: T) {
        dao.insert(thing)
    }

    override fun insert(things: List<T>) {
        Log.d("CommunityCache", "insert into ${this.javaClass}")
        for (thing in things) {
            dao.insert(thing)
        }
    }

    override fun update(thing: T) {
        dao.upsert(thing)
    }

    override suspend fun getAsync(name: Fullname): T? {
        return dao.getAsync(name)
    }

    override suspend fun delete(name: Fullname) {
        dao.delete(name)
    }

    suspend fun getBySlug(slug: String): T? {
        return dao.getBySlug(slug)
    }

    override fun get(name: Fullname): Flow<T?> {
        return dao.get(name)
    }

    override fun clear() {
        dao.deleteAll()
    }
}
