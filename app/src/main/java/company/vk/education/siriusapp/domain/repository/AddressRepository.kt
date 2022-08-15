package company.vk.education.siriusapp.domain.repository

import company.vk.education.siriusapp.domain.model.Location

interface AddressRepository {
    fun getLocationOfAnAddress(address: String): Location
    fun getAddressOfLocation(location: Location): String
}