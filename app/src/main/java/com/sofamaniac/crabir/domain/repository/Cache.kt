package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.coroutines.flow.Flow

interface Cache<T : DataInterface> {
    suspend fun insert(thing: T)
    suspend fun insert(things: List<T>)
    suspend fun update(thing: T)
    fun get(name: Fullname): Flow<T?>
    suspend fun getAsync(name: Fullname): T?
    suspend fun delete(name: Fullname)
    suspend fun clear()
}
