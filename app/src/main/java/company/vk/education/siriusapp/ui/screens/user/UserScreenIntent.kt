package company.vk.education.siriusapp.ui.screens.user

import androidx.compose.runtime.compositionLocalOf
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.ui.base.BaseViewIntent
import company.vk.education.siriusapp.ui.base.IntentConsumer

val LocalUserScreenIntentConsumer = compositionLocalOf {
    IntentConsumer<UserScreenIntent> {}
}

sealed class UserScreenIntent : BaseViewIntent {
    data class OnUserIdAcquired(val id: String?) : UserScreenIntent()
    data class ShowTripDetails(val trip: Trip) : UserScreenIntent()
}
