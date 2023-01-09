package com.help.android.domain.service

import com.help.android.domain.model.CurrentTripState
import kotlinx.coroutines.flow.StateFlow

interface CurrentTripService {
    val currentTripState: StateFlow<CurrentTripState>

    suspend fun setCurrentTrip(id: String)
    suspend fun clearCurrentTrip()
}