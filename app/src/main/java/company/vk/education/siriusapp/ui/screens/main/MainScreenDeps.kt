package company.vk.education.siriusapp.ui.screens.main

import company.vk.education.siriusapp.ui.activity.LocationPermissionHandler
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.BottomSheetMappers
import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalMainScreenDeps = unprovidedCompositionLocalOf<MainScreenDeps>()

class MainScreenDeps(
    val bottomSheetMappers: BottomSheetMappers,
    val locationPermissionHandler: LocationPermissionHandler
)
