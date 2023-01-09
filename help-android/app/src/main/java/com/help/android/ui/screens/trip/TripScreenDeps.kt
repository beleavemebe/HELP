package com.help.android.ui.screens.trip

import com.help.android.ui.utils.unprovidedCompositionLocalOf

val LocalTripScreenDeps = unprovidedCompositionLocalOf<TripScreenDeps>()

class TripScreenDeps(
    val mappers: TripScreenMappers
)