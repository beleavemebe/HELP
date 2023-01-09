package com.help.android.domain.repository

import com.help.android.domain.model.Trip
import com.help.android.domain.model.TripRoute
import kotlinx.coroutines.flow.Flow

interface TripsRepository {
    suspend fun getTrips(route: TripRoute): List<Trip>
    suspend fun getTripDetails(id: String): Trip
    fun getTripDetailsFlow(id: String): Flow<Trip>
    suspend fun joinTrip(trip: Trip)
    suspend fun publishTrip(trip: Trip)
    suspend fun cancelTrip(trip: Trip)
    suspend fun appendToTripHistory(userId: String, trip: Trip)
    suspend fun getTripHistory(userId: String): List<Trip>
}