package company.vk.education.siriusapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val tripsRepository: TripsRepository,
) : BaseViewModel<HomeViewState, HomeScreenIntent, HomeScreenError>() {
    override val initialState: HomeViewState
        get() = HomeViewState()

    override fun accept(intent: HomeScreenIntent): Any {
        return when (intent) {
            else -> {}
        }
    }

    override fun mapThrowable(throwable: Throwable): HomeScreenError {
        Log.d("HomeViewModel", "$throwable")
        return HomeScreenError.Unknown
    }
}