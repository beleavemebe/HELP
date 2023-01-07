package company.vk.education.siriusapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import company.vk.education.siriusapp.core.DtoMapper
import company.vk.education.siriusapp.domain.repository.Storage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject


open class DataStoreStorage<T> @Inject constructor(
    name: String,
    private val mapper: DtoMapper<T, String>,
    @ApplicationContext context: Context
) : Storage<T> {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name)
    private val datastore = context.dataStore
    private val dataKey = stringPreferencesKey("DATA")

    override suspend fun save(data: T) {
        datastore.edit {
            it[dataKey] = mapper.mapToDto(data)
        }
    }

    override fun readFlow() = datastore.data.map {
        data -> data[dataKey]?.let {
            mapper.mapToEntity(it)
        }
    }

    override suspend fun read(): T? = readFlow().lastOrNull()
}