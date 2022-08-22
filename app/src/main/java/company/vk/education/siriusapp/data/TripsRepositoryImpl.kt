package company.vk.education.siriusapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.TripsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val COLLECTION_TRIPS = "trips"

class TripsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val mapper: BiMapper<Trip, TripDto>
) : TripsRepository {

    override suspend fun getTrips(route: TripRoute): List<Trip> {
        val trips = db.collection(COLLECTION_TRIPS).get().await()
        return trips.toObjects(TripDto::class.java).map(mapper::mapFrom)
    }

    override suspend fun getTripDetails(id: String): Trip {
        val trip = db.collection(COLLECTION_TRIPS).document(id).get().await()
        return trip.toObject(TripDto::class.java)!!.let { mapper.mapFrom(it) }
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
