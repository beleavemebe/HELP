package company.vk.education.siriusapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface Storage<T> {
    suspend fun save(data: T)
    fun read() : Flow<T?>
}
