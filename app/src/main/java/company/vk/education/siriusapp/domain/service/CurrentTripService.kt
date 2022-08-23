package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.CurrentTripState
import kotlinx.coroutines.flow.Flow

interface CurrentTripService {
    val currentTripState: Flow<CurrentTripState>

    suspend fun setCurrentTrip(id: String)
    suspend fun clearCurrentTrip()
}