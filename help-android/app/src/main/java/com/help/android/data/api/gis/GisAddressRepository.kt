package com.help.android.data.api.gis

import com.help.android.BuildConfig
import com.help.android.domain.model.Location
import com.help.android.domain.repository.AddressRepository
import javax.inject.Inject

class GisAddressRepository @Inject constructor(
    private val gisGeocoderAPI: GisGeocoderAPI
) : AddressRepository {
    override suspend fun getAddressOfLocation(location: Location): String {
        val response = gisGeocoderAPI.convert(
            lat = location.latitude,
            lon = location.longitude,
            apikey = BuildConfig.GIS_GEOCODER_API_KEY
        )
        return response.result.items[0].name
    }
}