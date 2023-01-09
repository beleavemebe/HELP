package com.help.android.ui.screens.main

import com.help.android.ui.activity.LocationPermissionHandler
import com.help.android.ui.screens.main.bottomsheet.BottomSheetMappers
import com.help.android.ui.utils.unprovidedCompositionLocalOf

val LocalMainScreenDeps = unprovidedCompositionLocalOf<MainScreenDeps>()

class MainScreenDeps(
    val bottomSheetMappers: BottomSheetMappers,
    val locationPermissionHandler: LocationPermissionHandler
)
