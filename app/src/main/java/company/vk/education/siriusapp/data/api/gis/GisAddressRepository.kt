package company.vk.education.siriusapp.data.api.gis

import company.vk.education.siriusapp.BuildConfig
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
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