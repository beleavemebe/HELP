package company.vk.education.siriusapp.data

import com.google.firebase.firestore.FirebaseFirestore
import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.core.dist
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.TripsRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val COLLECTION_TRIPS = "trips"

class TripsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val mapper: BiMapper<Trip, TripDto>
) : TripsRepository {

    override suspend fun getTrips(route: TripRoute): List<Trip> {
        val trips = db.collection(COLLECTION_TRIPS).get().await()
        return trips.toObjects(TripDto::class.java)
            .map(mapper::mapFrom)
            .sortedBy { it.route dist route }
    }

    override suspend fun getTripDetails(id: String): Trip {
        val trip = db.collection(COLLECTION_TRIPS).document(id).get().await()
        return trip.toObject(TripDto::class.java)!!.let { mapper.mapFrom(it) }
    }

    override fun joinTrip(trip: Trip) {
        println("Not yet implemented")
    }

    override suspend fun publishTrip(trip: Trip) {
        db.collection(COLLECTION_TRIPS).add(mapper.mapTo(trip)).await()
    }

    override fun cancelTrip(trip: Trip) {
        println("Not yet implemented")
    }

    override fun getTripHistory(): List<Trip> {
        println("Not yet implemented")
        return emptyList()
    }
}
