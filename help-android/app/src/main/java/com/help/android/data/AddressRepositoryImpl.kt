package com.help.android.data

import com.help.android.BuildConfig
import com.help.android.core.Mapper
import com.help.android.data.api.yandex.AddressResponse
import com.help.android.data.api.yandex.GeocoderAPI
import com.help.android.data.api.yandex.Response
import com.help.android.domain.model.Location
import com.help.android.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val geocoderAPI: GeocoderAPI,
    private val geocoderMapper: Mapper<Response, AddressResponse>,
) : AddressRepository {
    override suspend fun getAddressOfLocation(location: Location): String {
        return geocoderMapper.map(
            geocoderAPI.convert(
                "${location.longitude} ${location.latitude}",
                BuildConfig.GEOCODER_API_KEY
            )
        ).name
    }

}