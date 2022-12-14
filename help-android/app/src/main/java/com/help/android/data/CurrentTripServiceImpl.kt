package com.help.android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.help.android.CurrentTripStateMsg
import com.help.android.core.Mapper
import com.help.android.domain.model.CurrentTripState
import com.help.android.domain.service.CurrentTripService
import com.help.android.ui.utils.log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CurrentTripServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mapper: Mapper<CurrentTripStateMsg, CurrentTripState>,
) : CurrentTripService {
    private val _currentTripState = MutableStateFlow(CurrentTripState())
    override val currentTripState = _currentTripState.asStateFlow()

    private val Context.currentTripStore: DataStore<CurrentTripStateMsg> by dataStore(
        fileName = DATA_STORE_FILE_NAME,
        serializer = CurrentTripStateMsgSerializer,
    )

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        context.currentTripStore.data.map {
            mapper.map(it)
        }.onEach {
            log("currentTripState updated $it")
            _currentTripState.value = it
        }.launchIn(scope)
    }

    override suspend fun setCurrentTrip(id: String) {
        log("setCurrentTrip $id")
        context.currentTripStore.updateData { currentTripState ->
            currentTripState.toBuilder().apply {
                tripId = id
            }.build()
        }
    }

    override suspend fun clearCurrentTrip() {
        log("clearCurrentTrip")
        context.currentTripStore.updateData { currentTripState ->
            currentTripState.toBuilder().clearTripId().build()
        }
    }
}