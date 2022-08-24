package company.vk.education.siriusapp.ui

import androidx.compose.runtime.compositionLocalOf
import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiServiceToStringResMapper
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiVehicleClassToStringResMapper
import company.vk.education.siriusapp.ui.screens.main.trip.TripScreenTitle

interface LocalMappersProvider {
    val taxiServiceMapper: Mapper<TaxiService, Int>
    val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int>
    val tripScreenTitleMapper: Mapper<TripScreenTitle, Int>
}

val LocalMappers = compositionLocalOf<LocalMappersProvider> {
    object : LocalMappersProvider {
        override val taxiServiceMapper: Mapper<TaxiService, Int> get() = error("")
        override val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int> get() = error("")
        override val tripScreenTitleMapper: Mapper<TripScreenTitle, Int> get() = error("")
    }
}
