package com.help.android.ui.screens.trip

import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.runtime.Error
import com.help.android.domain.model.Trip
import com.help.android.domain.model.TripRoute
import com.help.android.domain.repository.AddressRepository
import com.help.android.domain.repository.TripsRepository
import com.help.android.ui.base.BaseViewModel
import com.help.android.ui.screens.trip.model.TripScreenIntent
import com.help.android.ui.screens.trip.model.TripState
import com.help.android.ui.screens.trip.model.TripViewEffect
import com.help.android.ui.utils.log
import com.help.android.ui.utils.toPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class TripViewModel @Inject constructor(
    private val tripsRepository: TripsRepository,
    private val addressRepository: AddressRepository,
) : BaseViewModel<TripState, TripScreenIntent, Nothing, TripViewEffect>() {
    override val initialState = TripState()

    private val driveRouter by lazy { DirectionsFactory.getInstance().createDrivingRouter() }
    private var drivingSession: DrivingSession? = null

    private val routeListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
            if (routes.isEmpty()) return
            drawTripRoute(routes)
        }

        override fun onDrivingRoutesError(error: Error) {
//            drivingSession?.retry(this)
        }
    }

    private fun drawTripRoute(routes: MutableList<DrivingRoute>) = postViewEffect {
        TripViewEffect.DrawRoute(routes.first().geometry)
    }

    override fun consume(intent: TripScreenIntent): Any {
        log("Got intent: $intent")
        return when (intent) {
            is TripScreenIntent.OnTripIdAcquired -> loadTrip(intent.tripId)
            is TripScreenIntent.ShowUser -> {}
        }
    }

    private fun loadTrip(tripId: String) {
        tripsRepository.getTripDetailsFlow(tripId)
            .onEach { trip ->
                driveRoute(trip.route)
                renderTrip(trip)
            }.launchIn(viewModelScope)
    }

    private fun renderTrip(trip: Trip) = reduce {
        it.copy(
            isLoading = false,
            trip = trip,
            startAddress = addressRepository.getAddressOfLocation(trip.route.startLocation),
            endAddress = addressRepository.getAddressOfLocation(trip.route.endLocation),
        )
    }

    private fun driveRoute(route: TripRoute) {
        val list = listOf(
            RequestPoint(route.startLocation.toPoint(), RequestPointType.WAYPOINT, null),
            RequestPoint(route.endLocation.toPoint(), RequestPointType.WAYPOINT, null)
        )
        drivingSession = driveRouter.requestRoutes(
            list, DrivingOptions(), VehicleOptions(), routeListener
        )
    }
}
