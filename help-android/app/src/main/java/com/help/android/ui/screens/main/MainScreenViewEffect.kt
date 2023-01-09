package com.help.android.ui.screens.main

import com.yandex.mapkit.geometry.Point
import com.help.android.domain.model.Location
import com.help.android.ui.base.BaseViewEffect
import com.help.android.ui.base.ViewEffectSource
import com.help.android.ui.library.yandexmaps.MapViewEffect
import com.help.android.ui.utils.unprovidedCompositionLocalOf

val LocalMainScreenViewEffectSource = unprovidedCompositionLocalOf<ViewEffectSource<MainScreenViewEffect>>()

open class MainScreenViewEffect private constructor() : BaseViewEffect {
    data class MoveMapToLocation(val location: Location) : MainScreenViewEffect()
    data class Navigate(val route: String) : MainScreenViewEffect()
    object LocationPermissionGranted : MainScreenViewEffect()
}

fun toMapViewEffect(viewEffect: MainScreenViewEffect): MapViewEffect? = when (viewEffect) {
    is MainScreenViewEffect.LocationPermissionGranted -> MapViewEffect.LocationPermissionGranted
    is MainScreenViewEffect.MoveMapToLocation ->
        MapViewEffect.MoveToPoint(
            Point(viewEffect.location.latitude, viewEffect.location.longitude),
        )
    else -> null
}
