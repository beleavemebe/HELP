package company.vk.education.siriusapp.ui.screens.main

import androidx.lifecycle.viewModelScope
import company.vk.education.siriusapp.core.HourAndMinute
import company.vk.education.siriusapp.core.dist
import company.vk.education.siriusapp.core.meters
import company.vk.education.siriusapp.domain.model.*
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.domain.service.CurrentTripService
import company.vk.education.siriusapp.domain.service.LocationService
import company.vk.education.siriusapp.domain.service.ScheduledTripsService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.Screens
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreenState
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiPreference
import company.vk.education.siriusapp.ui.screens.main.map.MapViewState
import company.vk.education.siriusapp.ui.library.trips.TripCard
import company.vk.education.siriusapp.ui.library.trips.TripItemButtonState
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent.*
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent.BottomSheetIntent.*
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent.MapIntent.*
import company.vk.education.siriusapp.ui.screens.main.MainViewModel.InvalidInputException.*
import company.vk.education.siriusapp.ui.utils.log
import company.vk.education.siriusapp.ui.utils.setHourAndMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService,
    private val addressRepository: AddressRepository,
    private val tripsRepository: TripsRepository,
    private val scheduledTripsService: ScheduledTripsService,
    private val currentTripService: CurrentTripService,
    private val locationService: LocationService
) : BaseViewModel<MainScreenState, MainScreenIntent, Nothing, MainScreenViewEffect>() {
    override val initialState =
        MainScreenState(
            mapState = MapViewState(),
            bottomSheetScreenState = BottomSheetScreenState()
        )

    private val userId: String
        get() = authService.authState.value.user?.id
            ?: error("User id unknown")

    init {
        authService.authState
            .onEach(::refreshAuthInfo)
            .launchIn(viewModelScope)
        authService.auth()

        currentTripService.currentTripState
            .onEach(::displayCurrentTrip)
            .launchIn(viewModelScope)
    }

    private fun refreshAuthInfo(authState: AuthState) = reduce { prevState ->
        val newMapState = prevState.mapState.copy(profilePicUrl = authState.user?.imageUrl)
        prevState.copy(mapState = newMapState)
    }

    private fun displayCurrentTrip(currentTripState: CurrentTripState) {
        if (!currentTripState.isUnknown.not()) return

        currentTripState.currentTripId
            ?.takeUnless { it.isBlank() }
            ?.let(::jumpToCurrentTrip)
    }

    private fun jumpToCurrentTrip(currentTripId: String) = postViewEffect {
        MainScreenViewEffect.Navigate(Screens.Trip.buildRoute(currentTripId))
    }

    override fun consume(intent: MainScreenIntent): Any {
        log("Got intent: $intent")
        return when (intent) {
            is ShowMyProfile -> authOrViewMyProfile()
            is PickStartOnTheMap -> pickTripStart()
            is PickEndOnTheMap -> pickTripEnd()
            is PickTripDate -> pickTripDate()
            is PickTripTime -> pickTripTime()
            is PickTaxiPreference -> pickTaxiPreference(intent.preference)
            is DismissTaxiPreferenceMenu -> dismissTaxiPreferenceMenu(intent.preference)
            is AddressChosen -> {
                val addressLocation = intent.addressLocation
                val addressToChoose = intent.addressToChoose
                chooseAddress(addressLocation, addressToChoose)
            }
            is UpdatePickedLocation -> updatePickedLocation(intent.location)
            is TripDatePicked -> setTripDate(intent.date)
            is TripTimePicked -> setTripTime(intent.hourAndMinute)
            is UserLocationAcquired -> {
                locationService.setCurrentLocation(intent.location)
                setStartLocationIfNotAlready(intent.location)
            }
            is TaxiServicePicked -> setTaxiService(intent.taxiService)
            is TaxiVehicleClassPicked -> setTaxiVehicleClass(intent.taxiVehicleClass)
            is CreateTrip -> createTrip()
            is SetFreePlacesAmount -> setFreePlacesAmount(intent.freePlaces)
            is PublishTrip -> publishTrip()
            is CancelCreatingTrip -> cancelCreatingTrip()
            is ShowTripDetails -> openTripModalSheet(intent.trip)
            is JoinTrip -> joinTrip(intent.trip)
            is ShowUser -> showUser(intent.user)
            is CollapseBottomSheet -> collapseBottomSheet()
            is ExpandBottomSheet -> expandBottomSheet()
            is MoveToUserLocation -> moveMapToLocation(locationService.currentLocation.value)
            is InvalidateChosenAddress -> invalidateChosenLocation()
            is LocationPermissionGranted -> signalLocationPermissionGranted()
        }
    }

    private fun signalLocationPermissionGranted() = postViewEffect {
        MainScreenViewEffect.LocationPermissionGranted
    }

    private fun invalidateChosenLocation() = reduce { prevState ->
        prevState.copy(
            mapState = prevState.mapState.copy(
                currentlyChosenAddress = null,
                currentlyChosenLocation = null
            )
        )
    }

    private fun expandBottomSheet() = reduce { prevState ->
        prevState.copy(
            isBottomSheetExpanded = true
        )
    }

    private fun collapseBottomSheet() = reduce { prevState ->
        prevState.copy(
            isBottomSheetExpanded = false
        )
    }

    private fun joinTrip(trip: Trip) {
        execute {
            tripsRepository.joinTrip(trip)
            // todo implement on the backend
//            tripsRepository.appendToTripHistory(userId, trip)
//            scheduleTrip(trip)
        }

        reduce { prevState ->
            // todo replace with live updates in TripsRepositoryImpl
            val updatedTrips = tripsRepository.getTrips(trip.route)
            prevState.copy(
                bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                    trips = updatedTrips.toTripCards(trip.route)
                )
            )
        }

        postViewEffect {
            MainScreenViewEffect.Navigate(Screens.Trip.buildRoute(trip.id))
        }
    }


    private fun openTripModalSheet(trip: Trip) = postViewEffect {
        MainScreenViewEffect.Navigate(Screens.Trip.buildRoute(trip.id))
    }

    private fun cancelCreatingTrip() = reduce { prevState ->
        prevState.copy(
            bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                isSearchingTrips = true,
                freePlaces = null,
                taxiService = null,
                taxiVehicleClass = null,
            )
        )
    }

    private fun publishTrip() = reduce { prevState ->
        val sheetState = prevState.bottomSheetScreenState
        val result = buildTripFromInputs(sheetState)
        val trip = result.getOrElse { throwable ->
            return@reduce reflectInvalidInput(throwable, prevState, sheetState)
        }

        tripsRepository.publishTrip(trip)
        // todo implement on the backend
//        tripsRepository.appendToTripHistory(userId, trip)
//        scheduleTrip(trip)

        // todo replace with live updates in TripsRepositoryImpl
        val updatedTrips = tripsRepository.getTrips(trip.route)

        postViewEffect {
            MainScreenViewEffect.Navigate(Screens.Trip.buildRoute(trip.id))
        }

        prevState.copy(
            bottomSheetScreenState = sheetState.copy(
                trips = updatedTrips.toTripCards(trip.route),
                isSearchingTrips = true,
            ),
            isBottomSheetExpanded = false,
        )
    }

    private fun reflectInvalidInput(
        throwable: Throwable,
        prevState: MainScreenState,
        bss: BottomSheetScreenState
    ): MainScreenState = when (throwable as? InvalidInputException) {
        null -> error(
            "buildTripFromInputs returned an error other than InvalidInputException subclass"
        )
        is NotAuthenticated -> prevState
        is InvalidDate -> prevState.copy(
            bottomSheetScreenState = bss.copy(
                dateErrorMsg = "Unlocalized error message"
            )
        )
        is InvalidFreePlaces -> prevState.copy(
            bottomSheetScreenState = bss.copy(
                freePlacesErrorMsg = "Unlocalized error message"
            )
        )
        is MissingTaxiService -> prevState.copy(
            bottomSheetScreenState = bss.copy(
                taxiServiceErrorMsg = "Unlocalized error message"
            )
        )
        is MissingTaxiVehicleClass -> prevState.copy(
            bottomSheetScreenState = bss.copy(
                taxiVehicleClassErrorMsg = "Unlocalized error message"
            )
        )
    }

    sealed class InvalidInputException : Exception() {
        object InvalidDate : InvalidInputException()
        object InvalidFreePlaces : InvalidInputException()
        object NotAuthenticated : InvalidInputException()
        object MissingTaxiService : InvalidInputException()
        object MissingTaxiVehicleClass : InvalidInputException()
    }

    private fun buildTripFromInputs(state: BottomSheetScreenState): Result<Trip> {
        val user = authService.authState.value.user
            ?: return Result.failure(NotAuthenticated)

        val date = state.date
            ?: return Result.failure(InvalidDate)

        val freePlaces = state.freePlaces
            ?: return Result.failure(InvalidFreePlaces)

        val taxiService = state.taxiService
            ?: return Result.failure(MissingTaxiService)

        val taxiVehicleClass = state.taxiVehicleClass
            ?: return Result.failure(MissingTaxiVehicleClass)

        return Result.success(
            Trip(
                route = TripRoute(
                    startLocation = state.startLocation,
                    endLocation = state.endLocation,
                    date = date
                ),
                freePlaces = freePlaces,
                host = user,
                passengers = emptyList(),
                taxiService = taxiService,
                taxiVehicleClass = taxiVehicleClass
            )
        )
    }

    // todo implement on the backend
    private fun scheduleTrip(trip: Trip) = execute {
        return@execute
        scheduledTripsService.scheduleTripAt(
            trip.route.date,
            tripsRepository.getTripDetails(trip.id)
        )
    }

    private fun setFreePlacesAmount(freePlaces: Int) = reduce { prevState ->
        prevState.copy(
            bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                freePlaces = freePlaces
            )
        )
    }

    private fun createTrip() = reduce { prevState ->
        prevState.copy(
            bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                isSearchingTrips = false
            )
        )
    }

    private fun pickTaxiPreference(preference: TaxiPreference) = reduce { prevState ->
        when (preference) {
            TaxiPreference.TAXI_SERVICE -> prevState.copy(
                bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                    isShowingPickTaxiServiceMenu = true
                )
            )
            TaxiPreference.TAXI_VEHICLE_CLASS -> prevState.copy(
                bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                    isShowingPickTaxiVehicleClassMenu = true
                )
            )
        }
    }

    private fun dismissTaxiPreferenceMenu(preference: TaxiPreference) = reduce { prevState ->
        when (preference) {
            TaxiPreference.TAXI_SERVICE -> prevState.copy(
                bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                    isShowingPickTaxiServiceMenu = false
                )
            )
            TaxiPreference.TAXI_VEHICLE_CLASS -> prevState.copy(
                bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                    isShowingPickTaxiVehicleClassMenu = false
                )
            )
        }
    }

    private fun setTaxiService(taxiService: TaxiService) = reduce { prevState ->
        prevState.copy(
            bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                taxiService = taxiService,
                isShowingPickTaxiServiceMenu = false,
                taxiVehicleClass = null
            )
        )
    }

    private fun setTaxiVehicleClass(vehicleClass: TaxiVehicleClass) = reduce { prevState ->
        prevState.copy(
            bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                taxiVehicleClass = vehicleClass,
                isShowingPickTaxiVehicleClassMenu = false
            )
        )
    }

    private fun setStartLocationIfNotAlready(location: Location) {
        val isStartLocationAlreadyPicked =
            viewState.value.bottomSheetScreenState.startAddress.isBlank().not()
        if (isStartLocationAlreadyPicked) return

        execute {
            chooseAddress(location, AddressToChoose.START)
        }

        moveMapToLocation(location)
    }

    private fun moveMapToLocation(location: Location) = postViewEffect {
        if (location == Location.LOCATION_UNKNOWN) {
            null
        } else {
            MainScreenViewEffect.MoveMapToLocation(location)
        }
    }

    private fun authOrViewMyProfile() {
        val authState = authService.authState.value
//        if (authState.isUnknown) return
        if (authState.user == null) {
            authService.auth()
        } else {
            showUser(authState.user)
        }
    }

    private fun showUser(user: User) = postViewEffect {
        MainScreenViewEffect.Navigate(Screens.User.buildRoute(user.id))
    }

    private fun pickTripDate() = reduce { prevState ->
        val prevSheetState = prevState.bottomSheetScreenState
        prevState.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingDatePicker = true,
            )
        )
    }

    private fun setTripDate(date: Date?) = reduce { prevState ->
        val prevSheetState = prevState.bottomSheetScreenState
        val state = prevState.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingDatePicker = false,
                date = date
            )
        )
        checkIfAllFormsAreFilled(state)
    }

    private fun pickTripTime() = reduce { prevState ->
        val prevSheetState = prevState.bottomSheetScreenState
        if (prevSheetState.date == null) return@reduce prevState
        prevState.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingTimePicker = true
            )
        )
    }

    private fun setTripTime(hourAndMinute: HourAndMinute) = reduce { prevState ->
        val prevSheetState = prevState.bottomSheetScreenState
        require(prevSheetState.date != null) {
            "Cannot set time when the date is null"
        }

        val state = prevState.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingTimePicker = false,
                date = prevSheetState.date.setHourAndMinute(hourAndMinute)
            )
        )
        checkIfAllFormsAreFilled(state)
    }

    private fun updatePickedLocation(location: Location) = reduce { prevState ->
        if (prevState.mapState.isChoosingAddress.not()) {
            prevState
        } else {
            log(location.toString())
            val address = addressRepository.getAddressOfLocation(location)
            prevState.copy(
                mapState = prevState.mapState.copy(
                    currentlyChosenAddress = address,
                    currentlyChosenLocation = location
                )
            )
        }
    }

    private fun chooseAddress(
        addressLocation: Location,
        addressToChoose: AddressToChoose
    ) = reduce { prevState ->
        val address = addressRepository.getAddressOfLocation(addressLocation)
        val state = prevState.copy(
            mapState = prevState.mapState.copy(isChoosingAddress = false),
            bottomSheetScreenState = prevState.bottomSheetScreenState.run {
                when (addressToChoose) {
                    AddressToChoose.START -> copy(
                        startAddress = address,
                        startLocation = addressLocation
                    )
                    AddressToChoose.END -> copy(endAddress = address, endLocation = addressLocation)
                }
            }
        )
        checkIfAllFormsAreFilled(state)
    }


    private fun pickTripRoute(addressToChoose: AddressToChoose) {
        reduce { prevState ->
            val newMapState = prevState.mapState.copy(
                isChoosingAddress = true,
                addressToChoose = addressToChoose
            )
            prevState.copy(mapState = newMapState, isBottomSheetExpanded = false)
        }

        postViewEffect {
            val sheetState = viewState.value.bottomSheetScreenState
            when {
                addressToChoose == AddressToChoose.START && sheetState.startAddress.isNotBlank() -> {
                    MainScreenViewEffect.MoveMapToLocation(sheetState.startLocation)
                }
                addressToChoose == AddressToChoose.END && sheetState.endAddress.isNotBlank() -> {
                    MainScreenViewEffect.MoveMapToLocation(sheetState.endLocation)
                }
                else -> null
            }
        }
    }

    private fun pickTripStart() = pickTripRoute(AddressToChoose.START)

    private fun pickTripEnd() = pickTripRoute(AddressToChoose.END)

    private fun MainScreenState.allFormsAreFilled() = bottomSheetScreenState.run {
        val areAddressesSpecified = startAddress != "" && endAddress != ""
        val arePickersHidden = !isShowingDatePicker && !isShowingTimePicker
        areAddressesSpecified && arePickersHidden && date != null
    }

    private fun checkIfAllFormsAreFilled(state: MainScreenState): MainScreenState {
        return if (state.allFormsAreFilled().not()) {
            state
        } else {
            loadTrips(
                state.bottomSheetScreenState.startLocation,
                state.bottomSheetScreenState.endLocation,
                state.bottomSheetScreenState.date
                    ?: error("allFormsAreFilled returned true but date was null")
            )

            state.copy(
                isBottomSheetExpanded = true,
                bottomSheetScreenState = state.bottomSheetScreenState.copy(areTripsLoading = true)
            )
        }
    }

    private fun loadTrips(
        startLocation: Location,
        endLocation: Location,
        date: Date
    ) = reduce { prevState ->
        val route = TripRoute(startLocation, endLocation, date)
        val trips = tripsRepository.getTrips(route)
        prevState.copy(
            bottomSheetScreenState = prevState.bottomSheetScreenState.copy(
                areTripsLoading = false,
                trips = trips.toTripCards(route)
            )
        )
    }

    private suspend fun List<Trip>.toTripCards(
        route: TripRoute,
    ): List<TripCard> {
        val currentUser = authService.authState.value.user
        val currentTripId = currentTripService.currentTripState.value.currentTripId
        return map { trip ->
            TripCard(
                trip = trip,
                dist = round((route dist trip.route).meters()).toInt(),
                isCurrentTrip = trip.id == currentTripId,
                tripItemButtonState = when {
                    trip.host == currentUser -> TripItemButtonState.HOST
                    currentUser in trip.passengers -> TripItemButtonState.BOOKED
                    scheduledTripsService.isTripScheduledAt(trip.route.date) -> TripItemButtonState.CONFLICT
                    else -> TripItemButtonState.JOIN
                }
            )
        }
    }
}
