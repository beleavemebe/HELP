package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.Trip
import java.util.*

interface ScheduledTripsService {
    suspend fun scheduleTripAt(date: Date, trip: Trip)
    suspend fun isTripScheduledAt(date: Date): Boolean
}