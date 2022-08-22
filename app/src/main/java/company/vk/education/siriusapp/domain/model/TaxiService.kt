package company.vk.education.siriusapp.domain.model

interface TaxiVehicleClass {
    val alias: String
}

sealed interface TaxiService {
    val alias: String
    val classes: List<TaxiVehicleClass>

    object Yandex : TaxiService {
        override val alias = "yandex"
        override val classes = listOf(
            YandexVehicleClass.Economy,
            YandexVehicleClass.Comfort,
            YandexVehicleClass.ComfortPlus,
            YandexVehicleClass.Business,
            YandexVehicleClass.Premier,
            YandexVehicleClass.Cruise,
            YandexVehicleClass.Minivan,
        )

        sealed class YandexVehicleClass(override val alias: String) : TaxiVehicleClass {
            object Economy : YandexVehicleClass("economy")
            object Comfort : YandexVehicleClass("comfort")
            object ComfortPlus : YandexVehicleClass("comfort_plus")
            object Business : YandexVehicleClass("business")
            object Premier : YandexVehicleClass("premier")
            object Cruise : YandexVehicleClass("cruise")
            object Minivan : YandexVehicleClass("minivan")
        }
    }

    companion object {
        val SERVICES: List<TaxiService> = listOf(Yandex)
    }
}

fun findVehicleClass(taxiService: TaxiService, alias: String) = taxiService.classes.first { it.alias == alias }
fun findTaxiService(alias: String) = TaxiService.SERVICES.first { it.alias == alias }