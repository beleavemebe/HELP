package com.help.android.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val LOCATION_UNKNOWN = Location(Double.MIN_VALUE, Double.MIN_VALUE)
    }
}
