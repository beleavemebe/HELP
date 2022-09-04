package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
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