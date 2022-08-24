package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.Trip
import java.util.*

interface ScheduledTripsService {
    fun scheduleTripAt(date: Date, trip: Trip)
    fun isTripScheduledAt(date: Date): Boolean
}