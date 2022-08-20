package company.vk.education.siriusapp.ui.screens.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.mapview.MapView
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authService: AuthService,
    private val addressRepository: AddressRepository,
) : BaseViewModel<MainScreenState, MainScreenIntent, Nothing>() {
    override val initialState: MainScreenState
        get() = MainScreenState(
            mapState = MainViewState.MapViewState.Idle(),
            bottomSheetState = MainViewState.BottomSheetState.SearchTrips()
        )

    init {
        authService.authState
            .onEach { updateState(it) }
            .launchIn(viewModelScope)
    }

    private fun updateState(authState: AuthState) = reduce {
        val newMapState = when (val mapState = viewState.value.mapState) {
            is MainViewState.MapViewState.Idle -> mapState.copy(profilePicUrl = authState.user?.imageUrl)
            is MainViewState.MapViewState.ChoosingAddress -> mapState.copy(profilePicUrl = authState.user?.imageUrl)
        }

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
            is MainScreenIntent.MapIntent.LocationChosen -> {
                reduce {
                    it.copy(mapState = MainViewState.MapViewState.Idle())
                }
            }
        }
    }

    fun observeLocation(map: MapView) {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                reduce { it ->
                    when (val prevMapState = it.mapState) {
                        is MainViewState.MapViewState.Idle -> it/*.copy(
                            mapState = MainViewState.MapViewState.ChoosingAddress(
                                currentlyChosenAddress = addressRepository.getAddressOfLocation(
                                    Location(
                                        map.map.cameraPosition.target.latitude,
                                        map.map.cameraPosition.target.longitude
                                    )
                                ),
                                addressToChoose = AddressToChoose.START
                            )
                        )*/

                        is MainViewState.MapViewState.ChoosingAddress -> it.copy(
                            mapState = prevMapState.copy(
                                currentlyChosenAddress = addressRepository.getAddressOfLocation(
                                    Location(
                                        map.map.cameraPosition.target.latitude,
                                        map.map.cameraPosition.target.longitude
                                    )
                                )
                            )
                        )
                    }
                }
                delay(3000)
            }
        }
    }

    private fun pickTripStart() = reduce {
        Log.d("ViewModel", "pickTripStart")
        val newMapState = MainViewState.MapViewState.ChoosingAddress(
            authService.authState.value.user?.imageUrl,
            AddressToChoose.START,
            "Currently chosen address"
        )

        it.copy(mapState = newMapState)
    }

    private fun pickTripEnd(): Unit = reduce {
        Log.d("ViewModel", "pickTripEnd")
        val newMapState = MainViewState.MapViewState.ChoosingAddress(
            authService.authState.value.user?.imageUrl,
            AddressToChoose.END,
            "Currently chosen address"
        )

        it.copy(mapState = newMapState)
    }
}