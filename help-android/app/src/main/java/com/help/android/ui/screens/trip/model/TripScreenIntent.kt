package com.help.android.ui.screens.trip.model

import androidx.compose.runtime.compositionLocalOf
import com.help.android.domain.model.User
import com.help.android.ui.base.BaseViewIntent
import com.help.android.ui.base.IntentConsumer

val LocalTripScreenIntentConsumer = compositionLocalOf {
    IntentConsumer<TripScreenIntent> {}
}

sealed class TripScreenIntent : BaseViewIntent {
    data class OnTripIdAcquired(val tripId: String) : TripScreenIntent()
    data class ShowUser(val user: User): TripScreenIntent()
}
