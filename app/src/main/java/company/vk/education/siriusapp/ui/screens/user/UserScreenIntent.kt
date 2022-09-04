package company.vk.education.siriusapp.ui.screens.user

import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.base.BaseViewIntent
import company.vk.education.siriusapp.ui.base.IntentConsumer
import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalUserScreenIntentConsumer = unprovidedCompositionLocalOf<IntentConsumer<UserScreenIntent>>()

sealed class UserScreenIntent : BaseViewIntent {
    data class OnUserIdAcquired(val id: String?) : UserScreenIntent()
    data class ShowTripDetails(val trip: Trip) : UserScreenIntent()
}
