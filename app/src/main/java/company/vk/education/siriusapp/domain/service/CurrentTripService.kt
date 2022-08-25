package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.CurrentTripState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CurrentTripService {
    val currentTripState: StateFlow<CurrentTripState>

    suspend fun setCurrentTrip(id: String)
    suspend fun clearCurrentTrip()
}