package com.help.android.ui.screens.user

import androidx.compose.runtime.compositionLocalOf
import com.help.android.domain.model.Trip
import com.help.android.ui.base.BaseViewIntent
import com.help.android.ui.base.IntentConsumer

val LocalUserScreenIntentConsumer = compositionLocalOf {
    IntentConsumer<UserScreenIntent> {}
}

sealed class UserScreenIntent : BaseViewIntent {
    data class OnUserIdAcquired(val id: String?) : UserScreenIntent()
    data class ShowTripDetails(val trip: Trip) : UserScreenIntent()
}
