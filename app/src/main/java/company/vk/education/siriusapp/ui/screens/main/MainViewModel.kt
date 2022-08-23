package company.vk.education.siriusapp.ui.screens.main

import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.map.CameraListener
import company.vk.education.siriusapp.domain.model.*
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreenState
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiPreference
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.bottomSheetState
import company.vk.education.siriusapp.ui.screens.main.map.MapViewState
import company.vk.education.siriusapp.ui.utils.log
import company.vk.education.siriusapp.ui.utils.setHourAndMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService,
    private val addressRepository: AddressRepository,
    private val tripsRepository: TripsRepository,
) : BaseViewModel<MainScreenState, MainScreenIntent, Nothing, MainScreenViewEffect>() {
    override val initialState =
        MainScreenState(
            mapState = MapViewState(),
            bottomSheetScreenState = BottomSheetScreenState()
        )

    private val _cameraListener = CameraListener { _, cameraPosition, _, finished ->
        if (finished) {
            updatePickedLocation(
                Location(
                    cameraPosition.target.latitude,
                    cameraPosition.target.longitude
                )
            )
        }
    }

    val cameraListener: CameraListener
        get() = _cameraListener

    init {
        authService.authState
            .onEach(::updateState)
            .launchIn(viewModelScope)

        authService.auth()
    }

    private fun updateState(authState: AuthState) = reduce {
        val newMapState = it.mapState.copy(profilePicUrl = authState.user?.imageUrl)
        it.copy(mapState = newMapState)
    }

    override fun accept(intent: MainScreenIntent): Any {
        log("Got intent: $intent")
        return when (intent) {
            is MainScreenIntent.MapIntent.ShowProfile -> authOrViewMyProfile()
            is MainScreenIntent.BottomSheetIntent.PickStartOnTheMap -> pickTripStart()
            is MainScreenIntent.BottomSheetIntent.PickEndOnTheMap -> pickTripEnd()
            is MainScreenIntent.BottomSheetIntent.PickTripDate -> pickTripDate()
            is MainScreenIntent.BottomSheetIntent.PickTripTime -> pickTripTime()
            is MainScreenIntent.BottomSheetIntent.PickTaxiPreference -> pickTaxiPreference(intent.preference)
            is MainScreenIntent.BottomSheetIntent.DismissTaxiPreferenceMenu -> dismissTaxiPreferenceMenu(intent.preference)
            is MainScreenIntent.MapIntent.AddressChosen -> {
                val addressLocation = intent.addressLocation
                val addressToChoose = intent.addressToChoose
                chooseAddress(addressLocation, addressToChoose)
            }
            is MainScreenIntent.MapIntent.UpdatePickedLocation -> updatePickedLocation(intent.location)
            is MainScreenIntent.BottomSheetIntent.TripDatePicked -> setTripDate(intent.date)
            is MainScreenIntent.BottomSheetIntent.TripTimePicked -> setTripTime(intent.hourAndMinute)
            is MainScreenIntent.DismissUserModalSheet -> hideUserSheet()
            is MainScreenIntent.MapIntent.UserLocationAcquired -> setStartLocationIfNotAlready(intent.location)
            is MainScreenIntent.BottomSheetIntent.TaxiServicePicked -> setTaxiService(intent.taxiService)
            is MainScreenIntent.BottomSheetIntent.TaxiVehicleClassPicked -> setTaxiVehicleClass(intent.taxiVehicleClass)
            is MainScreenIntent.BottomSheetIntent.CreateTrip -> createTrip()
            is MainScreenIntent.BottomSheetIntent.SetFreePlacesAmount -> setFreePlacesAmount(intent.freePlaces)
        }
    }

    private fun setFreePlacesAmount(freePlaces: Int) = reduce {
        it.copy(
            bottomSheetScreenState = it.bottomSheetScreenState.copy(
                freePlaces = freePlaces
            )
        )
    }

    private fun createTrip() = reduce {
        it.copy(
            bottomSheetScreenState = it.bottomSheetScreenState.copy(
                isSearchingTrips = false
            )
        )
    }

    private fun pickTaxiPreference(preference: TaxiPreference) = reduce {
        when (preference) {
            TaxiPreference.TAXI_SERVICE -> it.copy(
                bottomSheetScreenState = it.bottomSheetScreenState.copy(
                    isShowingPickTaxiServiceMenu = true
                )
            )
            TaxiPreference.TAXI_VEHICLE_CLASS -> it.copy(
                bottomSheetScreenState = it.bottomSheetScreenState.copy(
                    isShowingPickTaxiVehicleClassMenu = true
    )
            )
        }
    }

    private fun dismissTaxiPreferenceMenu(preference: TaxiPreference) = reduce {
        when (preference) {
            TaxiPreference.TAXI_SERVICE -> it.copy(
                bottomSheetScreenState = it.bottomSheetScreenState.copy(
                    isShowingPickTaxiServiceMenu = false
                )
            )
            TaxiPreference.TAXI_VEHICLE_CLASS -> it.copy(
                bottomSheetScreenState = it.bottomSheetScreenState.copy(
                    isShowingPickTaxiVehicleClassMenu = false
                )
            )
        }
    }

    private fun setTaxiService(taxiService: TaxiService) = reduce {
        it.copy(
            bottomSheetScreenState = it.bottomSheetScreenState.copy(
                taxiService = taxiService,
                isShowingPickTaxiServiceMenu = false
            )
        )
    }

    private fun setTaxiVehicleClass(vehicleClass: TaxiVehicleClass) = reduce {
        it.copy(
            bottomSheetScreenState = it.bottomSheetScreenState.copy(
                taxiVehicleClass = vehicleClass,
                isShowingPickTaxiVehicleClassMenu = false
            )
        )
    }

    private fun setStartLocationIfNotAlready(location: Location) {
        val alreadyPickedStartLocation = viewState.value.bottomSheetScreenState.startAddress.isBlank().not()
        if (alreadyPickedStartLocation) return

        execute {
            chooseAddress(location, AddressToChoose.START)
        }

        viewEffect {
            MainScreenViewEffect.MoveMapToLocation(location)
        }
    }

    private fun hideUserSheet() = reduce {
        it.copy(isShowingProfile = false, profileToShow = null)
    }

    private fun authOrViewMyProfile() {
        val authState = authService.authState.value
        if (authState.isUnknown.not() && authState.user == null) {
            authService.auth()
        } else reduce {
            it.copy(isShowingProfile = true, profileToShow = authState.user)
        }
    }

    private fun pickTripDate() = reduce {
        val prevSheetState = it.bottomSheetScreenState
        it.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingDatePicker = true,
            )
        )
    }

    private fun setTripDate(date: Date?) = reduce {
        val prevSheetState = it.bottomSheetScreenState
        val state = it.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingDatePicker = false,
                date = date
            )
        )
        checkIfAllFormsAreFilled(state)
    }

    private fun pickTripTime() = reduce {
        val prevSheetState = it.bottomSheetScreenState
        if (prevSheetState.date == null) return@reduce it
        it.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingTimePicker = true
            )
        )
    }

    private fun setTripTime(hourAndMinute: HourAndMinute) = reduce {
        val prevSheetState = it.bottomSheetScreenState
        require(prevSheetState.date != null) {
            "Cannot set time when the date is null"
        }

        val state = it.copy(
            bottomSheetScreenState = prevSheetState.copy(
                isShowingTimePicker = false,
                date = prevSheetState.date.setHourAndMinute(hourAndMinute)
            )
        )
        checkIfAllFormsAreFilled(state)
    }

    private fun updatePickedLocation(location: Location) = reduce {
        if (viewState.value.mapState.isChoosingAddress.not()) {
            it
        } else {
            log(location.toString())
            val address = addressRepository.getAddressOfLocation(location)
            it.copy(mapState = it.mapState.copy(currentlyChosenAddress = address))
        }
    }

    private fun chooseAddress(
        addressLocation: Location,
        addressToChoose: AddressToChoose
    ) = reduce {
        val address = addressRepository.getAddressOfLocation(addressLocation)
        val state = it.copy(
            mapState = it.mapState.copy(isChoosingAddress = false),
            bottomSheetScreenState = it.bottomSheetScreenState.run {
                when (addressToChoose) {
                    AddressToChoose.START -> copy(startAddress = address, startLocation = addressLocation)
                    AddressToChoose.END -> copy(endAddress = address, endLocation = addressLocation)
                }
            }
        )
        checkIfAllFormsAreFilled(state)
    }


    private fun pickTripRoute(addressToChoose: AddressToChoose) {
        reduce {
            val newMapState = it.mapState.copy(
                isChoosingAddress = true,
                addressToChoose = addressToChoose
            )
            it.copy(mapState = newMapState, isBottomSheetExpanded = false)
        }

        viewEffect {
            val sheetState = viewState.value.bottomSheetScreenState
            when {
                addressToChoose == AddressToChoose.START && sheetState.startAddress.isNotBlank() -> {
                    MainScreenViewEffect.MoveMapToLocation(sheetState.startLocation)
                }
                addressToChoose == AddressToChoose.END && sheetState.endAddress.isNotBlank() ->  {
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
            execute {
                loadTrips(
                    state.bottomSheetScreenState.startAddress,
                    state.bottomSheetScreenState.endAddress,
                    state.bottomSheetScreenState.date
                        ?: error("allFormsAreFilled returned true but date was null")
                )
            }

            state.copy(
                isBottomSheetExpanded = true,
                bottomSheetScreenState = state.bottomSheetScreenState.copy(areTripsLoading = true)
            )
        }
    }

    private fun loadTrips(startAddress: String, endAddress: String, date: Date) = reduce {
        val startAddressLocation = addressRepository.getLocationOfAnAddress(startAddress)
        val endAddressLocation = addressRepository.getLocationOfAnAddress(endAddress)
        val trips =
            tripsRepository.getTrips(TripRoute(startAddressLocation, endAddressLocation, date))

        it.copy(
            bottomSheetScreenState = it.bottomSheetScreenState.copy(
                areTripsLoading = false,
                trips = trips
            )
        )
    }
}
