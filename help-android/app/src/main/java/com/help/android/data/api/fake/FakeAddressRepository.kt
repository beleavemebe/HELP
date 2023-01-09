package com.help.android.data.api.fake

import com.help.android.domain.model.Location
import com.help.android.domain.repository.AddressRepository
import javax.inject.Inject

class FakeAddressRepository @Inject constructor() : AddressRepository {
    override suspend fun getAddressOfLocation(location: Location) = "mock"
}