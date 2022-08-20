package company.vk.education.siriusapp.ui.screens.main

import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.map.CameraListener
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetState
import company.vk.education.siriusapp.ui.screens.main.map.MapViewState
import company.vk.education.siriusapp.ui.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _cameraListener = CameraListener { map, cameraPosition, cameraUpdateReason, finished ->
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
    }

    private fun updateState(authState: AuthState) = reduce {
        val newMapState = it.mapState.copy(profilePicUrl = authState.user?.imageUrl)
        it.copy(mapState = newMapState)
    }

    override fun accept(intent: MainScreenIntent): Any {
        return when (intent) {
            MainScreenIntent.MapIntent.ShowProfile -> authService.auth()
            MainScreenIntent.BottomSheetIntent.PickStartOnTheMap -> pickTripStart()
            MainScreenIntent.BottomSheetIntent.PickEndOnTheMap -> pickTripEnd()
            MainScreenIntent.BottomSheetIntent.PickTaxiService -> TODO()
            MainScreenIntent.BottomSheetIntent.PickTaxiVehicleClass -> TODO()
            MainScreenIntent.BottomSheetIntent.PickTripDate -> TODO()
            MainScreenIntent.BottomSheetIntent.PickTripTime -> TODO()
            is MainScreenIntent.MapIntent.AddressChosen -> {
                val addressLocation = intent.addressLocation
                val addressToChoose = intent.addressToChoose
                chooseAddress(addressLocation, addressToChoose)
            }
            is MainScreenIntent.MapIntent.UpdatePickedLocation -> updatePickedLocation(intent.location)
        }
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
        val newMapState = it.mapState.copy(isChoosingAddress = true, addressToChoose = AddressToChoose.START)
        it.copy(mapState = newMapState)
    }

    private fun pickTripEnd() = reduce {
        val newMapState = it.mapState.copy(isChoosingAddress = true, addressToChoose = AddressToChoose.END)
        it.copy(mapState = newMapState)
    }
}
