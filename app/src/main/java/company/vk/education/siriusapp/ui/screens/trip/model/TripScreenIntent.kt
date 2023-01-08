package company.vk.education.siriusapp.ui.screens.trip.model

import androidx.compose.runtime.compositionLocalOf
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.ui.base.BaseViewIntent
import company.vk.education.siriusapp.ui.base.IntentConsumer

val LocalTripScreenIntentConsumer = compositionLocalOf {
    IntentConsumer<TripScreenIntent> {}
}

sealed class TripScreenIntent : BaseViewIntent {
    data class OnTripIdAcquired(val tripId: String) : TripScreenIntent()
    data class ShowUser(val user: User): TripScreenIntent()
}
