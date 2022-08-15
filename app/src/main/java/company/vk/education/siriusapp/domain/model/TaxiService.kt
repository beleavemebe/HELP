package company.vk.education.siriusapp.domain.model

sealed class TaxiService {
    object Yandex : TaxiService() {
        enum class VehicleClasses {
            ECONOMY,
            COMFORT,
            COMFORT_PLUS,
            BUSINESS,
            PREMIER,
            CRUISE,
            MINIVAN
        }
    }
}
