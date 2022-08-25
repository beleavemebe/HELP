package company.vk.education.siriusapp.domain.repository

import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute

interface TripsRepository {
    suspend fun getTrips(route: TripRoute): List<Trip>
    suspend fun getTripDetails(id: String): Trip
    suspend fun joinTrip(trip: Trip)
    suspend fun publishTrip(trip: Trip)
    suspend fun cancelTrip(trip: Trip)
    suspend fun appendToTripHistory(userId: String, trip: Trip)
    suspend fun getTripHistory(userId: String): List<Trip>
}