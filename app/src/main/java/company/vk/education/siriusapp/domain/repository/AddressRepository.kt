package company.vk.education.siriusapp.domain.repository

import company.vk.education.siriusapp.domain.model.Location

interface AddressRepository {
    suspend fun getLocationOfAnAddress(address: String): Location
    suspend fun getAddressOfLocation(location: Location): String
}