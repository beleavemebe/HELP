package com.help.android.data

import com.help.android.domain.model.CurrentTripState
import com.help.android.domain.service.CurrentTripService
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