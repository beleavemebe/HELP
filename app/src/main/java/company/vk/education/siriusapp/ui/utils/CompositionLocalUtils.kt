package company.vk.education.siriusapp.ui.utils

import androidx.compose.runtime.compositionLocalOf
import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiServiceToStringResMapper
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiVehicleClassToStringResMapper

val LocalTaxiServiceToStringResMapper = compositionLocalOf<Mapper<TaxiService, Int>> {
    TaxiServiceToStringResMapper()
}
val LocalTaxiVehicleClassToStringResMapper = compositionLocalOf<Mapper<TaxiVehicleClass, Int>> {
    TaxiVehicleClassToStringResMapper()
}