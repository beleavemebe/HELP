package company.vk.education.siriusapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObjects
import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.core.dist
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val COLLECTION_TRIPS = "trips"

class TripsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val mapper: BiMapper<Trip, TripDto>,
    private val authService: AuthService,
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

    override suspend fun joinTrip(trip: Trip) {
        val user = authService.authState.value.user
        require(user != null) {
            "Joining trips without authentication is not implemented"
        }

        val isUserAlreadyInTheTrip = trip.passengers.any { it.id == user.id }
        if (isUserAlreadyInTheTrip) return

        val newTrip = trip.copy(
            passengers = trip.passengers + user,
            freePlaces = trip.freePlaces - 1
        )

        db.collection(COLLECTION_TRIPS).document(trip.id).set(mapper.mapTo(newTrip)).await()
    }

    override suspend fun publishTrip(trip: Trip) {
        db.collection(COLLECTION_TRIPS).document(trip.id).set(mapper.mapTo(trip)).await()
    }

    override fun cancelTrip(trip: Trip) {
        println("Not yet implemented")
    }

    override fun getTripHistory(): List<Trip> {
        println("Not yet implemented")
        return emptyList()
    }
}
