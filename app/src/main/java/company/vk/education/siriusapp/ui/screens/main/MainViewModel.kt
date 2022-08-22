package company.vk.education.siriusapp.ui.screens.main

import androidx.compose.material.BottomSheetState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.map.CameraListener
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.model.TripRoute
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetScreenState
import company.vk.education.siriusapp.ui.screens.main.map.MapViewState
import company.vk.education.siriusapp.ui.utils.log
import company.vk.education.siriusapp.ui.utils.setHourAndMinute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService,
    private val addressRepository: AddressRepository,
    private val tripsRepository: TripsRepository,
) : BaseViewModel<MainScreenState, MainScreenIntent, Nothing>() {
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
        return when (intent) {
            is MainScreenIntent.MapIntent.ShowProfile -> authService.auth()
            is MainScreenIntent.BottomSheetIntent.PickStartOnTheMap -> pickTripStart()
            is MainScreenIntent.BottomSheetIntent.PickEndOnTheMap -> pickTripEnd()
            is MainScreenIntent.BottomSheetIntent.PickTripDate -> pickTripDate()
            is MainScreenIntent.BottomSheetIntent.PickTripTime -> pickTripTime()
            is MainScreenIntent.BottomSheetIntent.PickTaxiService -> TODO()
            is MainScreenIntent.BottomSheetIntent.PickTaxiVehicleClass -> TODO()
            is MainScreenIntent.MapIntent.AddressChosen -> {
                val addressLocation = intent.addressLocation
                val addressToChoose = intent.addressToChoose
                chooseAddress(addressLocation, addressToChoose)
            }
            is MainScreenIntent.MapIntent.UpdatePickedLocation -> updatePickedLocation(intent.location)
            is MainScreenIntent.BottomSheetIntent.TripDatePicked -> setTripDate(intent.date)
            is MainScreenIntent.BottomSheetIntent.TripTimePicked -> setTripTime(intent.hourAndMinute)
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
            isBottomSheetExpanded = addressToChoose == AddressToChoose.END,
            mapState = it.mapState.copy(isChoosingAddress = false),
            bottomSheetScreenState = it.bottomSheetScreenState.run {
                when (addressToChoose) {
                    AddressToChoose.START -> copy(startAddress = address)
                    AddressToChoose.END -> copy(endAddress = address)
                }
            }
        )
        checkIfAllFormsAreFilled(state)
    }


    private fun pickTripRoute(adressToChoose: AddressToChoose) = reduce {
        val newMapState = it.mapState.copy(
            isChoosingAddress = true,
            addressToChoose = adressToChoose
        )
        it.copy(mapState = newMapState, isBottomSheetExpanded = false)
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
            viewModelScope.launch {
                loadTrips(
                    state.bottomSheetScreenState.startAddress,
                    state.bottomSheetScreenState.endAddress,
                    state.bottomSheetScreenState.date
                        ?: error("allFormsAreFilled returned true but date was null")
                )
            }

            state.copy(bottomSheetScreenState = state.bottomSheetScreenState.copy(areTripsLoading = true))
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
