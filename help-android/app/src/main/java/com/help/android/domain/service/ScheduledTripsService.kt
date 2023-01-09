package com.help.android.domain.service

import com.help.android.domain.model.Trip
import java.util.*

interface ScheduledTripsService {
    suspend fun scheduleTripAt(date: Date, trip: Trip)
    suspend fun isTripScheduledAt(date: Date): Boolean
}