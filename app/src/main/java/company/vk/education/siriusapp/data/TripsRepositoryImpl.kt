package company.vk.education.siriusapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.TripsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TripsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val mapper: BiMapper<Trip, TripDto>
) : TripsRepository {

    override suspend fun getTrips(route: TripRoute): List<Trip> {
        val trips = db.collection("trips").get().await()
        return trips.toObjects(TripDto::class.java).map(mapper::mapFrom)
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
