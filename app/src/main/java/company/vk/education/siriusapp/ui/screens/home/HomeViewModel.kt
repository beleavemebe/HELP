package company.vk.education.siriusapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val tripsRepository: TripsRepository,
    private val addressRepository: AddressRepository
) : BaseViewModel<HomeViewState, HomeScreenIntent, HomeScreenError>() {
    override val initialState: HomeViewState
        get() = HomeViewState()

    override fun accept(intent: HomeScreenIntent): Any {
        return when (intent) {
            else -> {}
        }
    }

    fun consume(string: String) {
        viewModelScope.launch {
            Log.d("RESPONSE", addressRepository.getLocationOfAnAddress(string).toString())
        }
    }

    fun consume(location: Location) {
        viewModelScope.launch {
            Log.d("RESPONSE", addressRepository.getAddressOfLocation(location))
        }
    }

    override fun mapThrowable(throwable: Throwable): HomeScreenError {
        Log.d("HomeViewModel", "$throwable")
        return HomeScreenError.Unknown
    }
}