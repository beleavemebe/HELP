package company.vk.education.siriusapp.data

import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.TripsRepository

class TripsRepositoryImpl : TripsRepository {
    override fun getTrips(route: TripRoute) {
        println("Not yet implemented")
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
