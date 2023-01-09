package com.help.android.ui.screens.main.bottomsheet

import com.help.android.R
import com.help.android.core.Mapper
import com.help.android.domain.model.TaxiService
import com.help.android.domain.model.TaxiVehicleClass
import javax.inject.Inject

class TaxiVehicleClassToStringResMapper @Inject constructor(
) : Mapper<TaxiVehicleClass, Int> {
    override fun map(arg: TaxiVehicleClass): Int {
        return when (arg) {
            TaxiService.Yandex.YandexVehicleClass.Business -> R.string.business
            TaxiService.Yandex.YandexVehicleClass.Comfort -> R.string.comfort
            TaxiService.Yandex.YandexVehicleClass.ComfortPlus -> R.string.comfort_plus
            TaxiService.Yandex.YandexVehicleClass.Cruise -> R.string.cruise
            TaxiService.Yandex.YandexVehicleClass.Economy -> R.string.economy
            TaxiService.Yandex.YandexVehicleClass.Minivan -> R.string.minivan
            TaxiService.Yandex.YandexVehicleClass.Premier -> R.string.premier
            TaxiService.Uber.UberVehicleClass.UberX -> R.string.uber_uberx
            TaxiService.Uber.UberVehicleClass.Select -> R.string.uber_select
            TaxiService.Uber.UberVehicleClass.Kids -> R.string.uber_kids
        }
    }
}