package company.vk.education.siriusapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface Storage<T> {
    suspend fun save(data: T)
    suspend fun read(): T?
    fun readFlow(): Flow<T?>
}
