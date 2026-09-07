package com.sofamaniac.crabir.domain.repository

import com.sofamaniac.crabir.domain.model.Fullname
import kotlinx.coroutines.flow.Flow

interface Cache<T : DataInterface> {
    fun insert(thing: T)
    fun insert(things: List<T>)
    fun update(thing: T)
    fun get(name: Fullname): Flow<T?>
    suspend fun getAsync(name: Fullname): T?
    suspend fun delete(name: Fullname)
    fun clear()
}
