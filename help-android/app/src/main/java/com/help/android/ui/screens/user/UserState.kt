package com.help.android.ui.screens.user

import com.help.android.domain.model.Trip
import com.help.android.domain.model.User
import com.help.android.ui.base.BaseViewState

data class UserState(
    val isLoading: Boolean = true,
    val user: User? = null,
    val currentTrip: Trip? = null,
    val scheduledTrips: List<Trip> = emptyList(),
    val previousTrips: List<Trip> = emptyList(),
) : BaseViewState
