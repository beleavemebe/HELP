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
) : BaseViewModel<HomeViewState, HomeScreenError>() {
    private val _viewState = MutableStateFlow(HomeViewState())
    override val viewState: StateFlow<HomeViewState> = _viewState

    override fun mapThrowable(throwable: Throwable): HomeScreenError {
        Log.d("HomeViewModel", "$throwable")
        return HomeScreenError.Unknown
    }

    private inline fun reduce(f: (HomeViewState) -> HomeViewState) {
        _viewState.value = f(viewState.value)
    }

}