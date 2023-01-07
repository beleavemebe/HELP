package company.vk.education.siriusapp.data

import com.google.firebase.firestore.FirebaseFirestore
import company.vk.education.siriusapp.core.DtoMapper
import company.vk.education.siriusapp.core.dist
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.data.model.TripHistory
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val COLLECTION_TRIPS = "trips"
private const val COLLECTION_TRIP_HISTORY = "trip_history"

class TripsRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val tripMapper: DtoMapper<Trip, TripDto>,
    private val authService: AuthService,
) : TripsRepository {
    override suspend fun getTrips(route: TripRoute): List<Trip> {
        val trips = db.collection(COLLECTION_TRIPS).get().await()
        val selectDist: (Trip) -> Double = { it.route dist route }
        return trips.toObjects(TripDto::class.java)
            .map(tripMapper::mapToEntity)
            .filter { it.route.date >= route.date }
            .sortedWith(
                Comparator<Trip> { first, second ->
                    first.route.date.compareTo(second.route.date)
                }.thenComparing { lhs, rhs ->
                    selectDist(lhs).compareTo(selectDist(rhs))
                }
            )
    }

    override suspend fun getTripDetails(id: String): Trip {
        val trip = db.collection(COLLECTION_TRIPS).document(id).get().await()
        return trip.toObject(TripDto::class.java)!!.let { tripMapper.mapToEntity(it) }
    }

    override suspend fun joinTrip(trip: Trip) {
        val user = authService.authState.value.user
        require(user != null) {
            "Joining trips without authentication is not implemented"
        }

        val isUserTheHost = trip.host == user
        if (isUserTheHost) return

        val isUserAlreadyInTheTrip = trip.passengers.any { it.id == user.id }
        if (isUserAlreadyInTheTrip) return

        val newTrip = trip.copy(
            passengers = trip.passengers + user,
            freePlaces = trip.freePlaces - 1
        )

        db.collection(COLLECTION_TRIPS).document(trip.id).set(tripMapper.mapToDto(newTrip)).await()
    }

    override suspend fun publishTrip(trip: Trip) {
        db.collection(COLLECTION_TRIPS).document(trip.id).set(tripMapper.mapToDto(trip)).await()
    }

    override suspend fun cancelTrip(trip: Trip) {
        println("Not yet implemented")
    }

    override suspend fun getTripHistory(userId: String): List<Trip> {
        return db.collection(COLLECTION_TRIP_HISTORY).document(userId).get().await()
            .toObject(TripHistory::class.java)
            ?.history
            ?.map(tripMapper::mapToEntity)
            ?: emptyList()
    }

    // todo implement on the backend
    override suspend fun appendToTripHistory(userId: String, trip: Trip) {
        val prevHistory = getTripHistory(userId)
        val newHistory = prevHistory + trip

        db.collection(COLLECTION_TRIP_HISTORY).document(userId).set(
            TripHistory(newHistory.map(tripMapper::mapToDto))
        ).await()
    }

    override fun getTripDetailsFlow(id: String): Flow<Trip> {
        return callbackFlow {
            db.collection(COLLECTION_TRIPS).document(id)
                .addSnapshotListener { value, _ ->
                    value?.toObject(TripDto::class.java)
                        ?.let { tripMapper.mapToEntity(it) }
                        ?.let { trySend(it) }
                }

            awaitClose()
        }
    }
}
