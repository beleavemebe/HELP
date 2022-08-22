package company.vk.education.siriusapp.domain.model

sealed class TaxiService(val alias: String) {
    object Yandex : TaxiService("yandex") {
        enum class VehicleClasses(val alias: String) {
            ECONOMY("economy"),
            COMFORT("comfort"),
            COMFORT_PLUS("comfort_plus"),
            BUSINESS("business"),
            PREMIER("premier"),
            CRUISE("cruise"),
            MINIVAN("minivan")
        }
    }
}
