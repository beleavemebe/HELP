package company.vk.education.siriusapp.ui.screens.trip

import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalTripScreenDeps = unprovidedCompositionLocalOf<TripScreenDeps>()

class TripScreenDeps(
    val mappers: TripScreenMappers
)