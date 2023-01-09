package com.help.android.ui.screens.trip.model

import com.yandex.mapkit.geometry.Polyline
import com.help.android.ui.base.BaseViewEffect
import com.help.android.ui.base.ViewEffectSource
import com.help.android.ui.library.yandexmaps.MapViewEffect
import com.help.android.ui.utils.unprovidedCompositionLocalOf

val LocalTripViewEffectSource = unprovidedCompositionLocalOf<ViewEffectSource<TripViewEffect>>()

sealed class TripViewEffect : BaseViewEffect {
    data class DrawRoute(val polyline: Polyline) : TripViewEffect()
}

fun toMapViewEffect(viewEffect: TripViewEffect): MapViewEffect = when (viewEffect) {
    is TripViewEffect.DrawRoute -> MapViewEffect.RenderRoute(
        polyline = viewEffect.polyline,
        moveToRoute = true
    )
}