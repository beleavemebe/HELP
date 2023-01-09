package company.vk.education.siriusapp.data

import company.vk.education.siriusapp.domain.model.CurrentTripState
import company.vk.education.siriusapp.domain.service.CurrentTripService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCurrentTripService : CurrentTripService {
    override val currentTripState = MutableStateFlow(CurrentTripState()).asStateFlow()

    override suspend fun setCurrentTrip(id: String) {
        return
    }

    override suspend fun clearCurrentTrip() {
        return
    }
}