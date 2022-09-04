package company.vk.education.siriusapp.ui.screens.main

import com.yandex.mapkit.geometry.Point
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.ui.base.BaseViewEffect
import company.vk.education.siriusapp.ui.base.ViewEffectSource
import company.vk.education.siriusapp.ui.library.yandexmaps.MapViewEffect
import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalMainScreenViewEffectSource = unprovidedCompositionLocalOf<ViewEffectSource<MainScreenViewEffect>>()

open class MainScreenViewEffect private constructor() : BaseViewEffect {
    data class MoveMapToLocation(val location: Location) : MainScreenViewEffect()
    data class Navigate(val route: String) : MainScreenViewEffect()
}

fun toMapViewEffect(viewEffect: MainScreenViewEffect): MapViewEffect? = when (viewEffect) {
    is MainScreenViewEffect.MoveMapToLocation ->
        MapViewEffect.MoveToPoint(
            Point(viewEffect.location.latitude, viewEffect.location.longitude),
        )
    else -> null
}
