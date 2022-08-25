package company.vk.education.siriusapp.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.screens.main.trip.TripCardButtonState
import company.vk.education.siriusapp.ui.screens.main.trip.TripScreenTitle

interface LocalMappersProvider {
    val taxiServiceMapper: Mapper<TaxiService, Int>
    val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int>
    val tripScreenTitleMapper: Mapper<TripScreenTitle, Int>
    val tripCardButtonTextMapper: Mapper<TripCardButtonState, Int>
    val tripCardButtonColorMapper: Mapper<TripCardButtonState, Color>
}

val LocalMappers = compositionLocalOf<LocalMappersProvider> {
    object : LocalMappersProvider {
        override val taxiServiceMapper: Mapper<TaxiService, Int> get() = error("")
        override val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int> get() = error("")
        override val tripScreenTitleMapper: Mapper<TripScreenTitle, Int> get() = error("")
        override val tripCardButtonTextMapper: Mapper<TripCardButtonState, Int> get() = error("")
        override val tripCardButtonColorMapper: Mapper<TripCardButtonState, Color> get() = error("")
    }
}
