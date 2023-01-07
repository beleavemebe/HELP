package company.vk.education.siriusapp.ui.screens.trip.model

import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.ui.base.BaseViewIntent
import company.vk.education.siriusapp.ui.base.IntentConsumer
import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalTripScreenIntentConsumer = unprovidedCompositionLocalOf<IntentConsumer<TripScreenIntent>>()

sealed class TripScreenIntent : BaseViewIntent {
    data class OnTripIdAcquired(val tripId: String) : TripScreenIntent()
    data class ShowUser(val user: User): TripScreenIntent()
}
