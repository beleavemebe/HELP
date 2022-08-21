package company.vk.education.siriusapp.data

import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.TripsRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class TripsRepositoryImpl @Inject constructor() : TripsRepository {
    override suspend fun getTrips(route: TripRoute): List<Trip> {
        delay(1000L)
        return emptyList()
    }

    override fun getTripDetails(id: String): Trip {
        println("Not yet implemented")
        TODO()
    }

    override fun joinTrip(trip: Trip) {
        println("Not yet implemented")
    }

    override fun publishTrip(trip: Trip) {
        println("Not yet implemented")
    }

    override fun cancelTrip(trip: Trip) {
        println("Not yet implemented")
    }

    override fun getTripHistory(): List<Trip> {
        println("Not yet implemented")
        return emptyList()
    }
}
