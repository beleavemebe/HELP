package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalTaxiInfoMappers = unprovidedCompositionLocalOf<TaxiInfoMappers>()

interface TaxiInfoMappers {
    val taxiServiceMapper: Mapper<TaxiService, Int>
    val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int>
}