package company.vk.education.siriusapp.domain.repository

import company.vk.education.siriusapp.domain.model.Location

object AddressRepositoryImpl : AddressRepository {
    override fun getLocationOfAnAddress(address: String): Location {
        return Location.LOCATION_UNKNOWN
    }

    override fun getAddressOfLocation(location: Location): String {
        return "Not yet implemented"
    }
}