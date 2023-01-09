package com.help.android.data.model

data class TripDto(
    val id: String? = null,
    val startLatitude: Double? = null,
    val startLongitude: Double? = null,
    val endLatitude: Double? = null,
    val endLongitude: Double? = null,
    val dateMillis: Long? = null,
    val freePlaces: Int? = null,
    val host: UserDto? = null,
    val passengers: List<UserDto>? = null,
    val taxiService: String? = null,
    val taxiVehicleClass: String? = null,
)
