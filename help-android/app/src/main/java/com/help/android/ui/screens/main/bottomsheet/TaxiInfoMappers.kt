package com.help.android.ui.screens.main.bottomsheet

import com.help.android.core.Mapper
import com.help.android.domain.model.TaxiService
import com.help.android.domain.model.TaxiVehicleClass
import com.help.android.ui.utils.unprovidedCompositionLocalOf

val LocalTaxiInfoMappers = unprovidedCompositionLocalOf<TaxiInfoMappers>()

interface TaxiInfoMappers {
    val taxiServiceMapper: Mapper<TaxiService, Int>
    val taxiVehicleClassMapper: Mapper<TaxiVehicleClass, Int>
}