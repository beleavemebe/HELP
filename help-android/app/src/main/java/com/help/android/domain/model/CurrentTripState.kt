package com.help.android.domain.model

data class CurrentTripState(
    val isUnknown: Boolean = true,
    val currentTripId: String? = null,
)