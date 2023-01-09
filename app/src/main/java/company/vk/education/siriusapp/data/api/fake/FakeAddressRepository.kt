package company.vk.education.siriusapp.data.api.fake

import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
import javax.inject.Inject

class FakeAddressRepository @Inject constructor() : AddressRepository {
    override suspend fun getAddressOfLocation(location: Location) = "mock"
}