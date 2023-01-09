package com.help.android.domain.repository

import com.help.android.domain.model.Location

interface AddressRepository {
    suspend fun getAddressOfLocation(location: Location): String
}