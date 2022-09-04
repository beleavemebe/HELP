package company.vk.education.siriusapp.ui.screens.user

import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.base.BaseViewModel
import company.vk.education.siriusapp.ui.screens.Screens
import company.vk.education.siriusapp.ui.screens.main.trip.TripScreen
import company.vk.education.siriusapp.ui.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authService: AuthService,
    private val tripsRepository: TripsRepository,
) : BaseViewModel<UserState, UserScreenIntent, Nothing, UserScreenViewEffect>() {
    override val initialState: UserState = UserState()

    override fun consume(intent: UserScreenIntent): Any {
        log("Got intent: $intent")
        return when (intent) {
            is UserScreenIntent.OnUserIdAcquired -> loadUser(intent.id)
            is UserScreenIntent.ShowTripDetails -> showTrip(intent.trip.id)
        }
    }

    private fun showTrip(id: String) = viewEffect {
        UserScreenViewEffect.Navigate(Screens.Trip.buildRoute(id))
    }

    private fun loadUser(id: String?) = reduce { prevState ->
        val user = authService.authState.value.user ?: return@reduce prevState
        val tripHistory = tripsRepository.getTripHistory(user.id)
        prevState.copy(
            isLoading = false,
            user = user,
            scheduledTrips = tripHistory.filter { it.route.date >= Date() },
            previousTrips = tripHistory.filter { it.route.date < Date() }
        )
    }
}
