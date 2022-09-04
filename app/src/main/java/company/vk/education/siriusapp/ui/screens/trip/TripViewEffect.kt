package company.vk.education.siriusapp.ui.screens.trip

import com.yandex.mapkit.geometry.Polyline
import company.vk.education.siriusapp.ui.base.BaseViewEffect
import company.vk.education.siriusapp.ui.base.ViewEffectSource
import company.vk.education.siriusapp.ui.library.yandexmaps.MapViewEffect
import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

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