package com.help.android.ui.screens.trip.model

import com.yandex.mapkit.geometry.Polyline
import com.help.android.domain.model.Trip
import com.help.android.ui.base.BaseViewState

data class TripState(
    val isLoading: Boolean = true,
    val trip: Trip? = null,
    val startAddress: String? = null,
    val endAddress: String? = null,
    val tripRoutePolyline: Polyline? = null,
    val title: TripScreenTitle = TripScreenTitle.TRIP_DETAILS,
    val showControls: Boolean = false,
) : BaseViewState