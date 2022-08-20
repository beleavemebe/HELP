package company.vk.education.siriusapp.ui.screens.main

import androidx.lifecycle.viewModelScope
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetState
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
) : BaseViewModel<MainScreenState, MainScreenIntent, Nothing>() {
    override val initialState =
        MainScreenState(
            mapState = MapViewState(),
            bottomSheetState = BottomSheetState()
        )

    init {
        authService.authState
            .onEach(::updateState)
            .launchIn(viewModelScope)
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
        val prevSheetState = it.bottomSheetState
        it.copy(
            bottomSheetState = prevSheetState.copy(
                isShowingDatePicker = true,
            )
        )
    }

    private fun setTripDate(date: Date?) = reduce {
        val prevSheetState = it.bottomSheetState
        it.copy(
            bottomSheetState = prevSheetState.copy(
                isShowingDatePicker = false,
                date = date
            )
        )
    }

    private fun pickTripTime() = reduce {
        val prevSheetState = it.bottomSheetState
        if (prevSheetState.date == null) return@reduce it
        it.copy(
            bottomSheetState = prevSheetState.copy(
                isShowingTimePicker = true
            )
        )
    }

    private fun setTripTime(hourAndMinute: HourAndMinute) = reduce {
        val prevSheetState = it.bottomSheetState
        require(prevSheetState.date != null) {
            "Cannot set time when the date is null"
        }

        it.copy(
            bottomSheetState = prevSheetState.copy(
                isShowingTimePicker = false,
                date = prevSheetState.date.setHourAndMinute(hourAndMinute)
            )
        )
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
        it.copy(
            mapState = MapViewState(isChoosingAddress = false),
            bottomSheetState = run {
                val bss = it.bottomSheetState
                when (addressToChoose) {
                    AddressToChoose.START -> bss.copy(startAddress = address)
                    AddressToChoose.END -> bss.copy(endAddress = address)
                }
            }
        )
    }

    private fun pickTripStart() = reduce {
        val newMapState = it.mapState.copy(
            isChoosingAddress = true,
            addressToChoose = AddressToChoose.START
        )
        it.copy(mapState = newMapState)
    }

    private fun pickTripEnd() = reduce {
        val newMapState = it.mapState.copy(
            isChoosingAddress = true,
            addressToChoose = AddressToChoose.END
        )
        it.copy(mapState = newMapState)
    }
}
