package company.vk.education.siriusapp.ui.screens.main.map

import androidx.lifecycle.viewModelScope
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.main.MainScreenIntent
import company.vk.education.siriusapp.ui.screens.main.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val authService: AuthService
) : BaseViewModel<MainViewState.MapViewState, MainScreenIntent.MapIntent, Nothing>() {

    init {
        authService.authState
            .onEach(::updateState)
            .launchIn(viewModelScope)
    }

    private fun updateState(authState: AuthState) = reduce {
        when (it) {
            is MainViewState.MapViewState.Idle -> it.copy(profilePicUrl = authState.user?.imageUrl)
            is MainViewState.MapViewState.ChoosingAddress -> it.copy(profilePicUrl = authState.user?.imageUrl)
        }
    }

    override val initialState: MainViewState.MapViewState
        get() = MainViewState.MapViewState.Idle()

    override fun accept(intent: MainScreenIntent.MapIntent): Any {
        return when (intent) {
            is MainScreenIntent.MapIntent.ShowProfile -> TODO() //authService.authViaVk()
        }
    }
}