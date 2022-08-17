package company.vk.education.siriusapp.domain.repository

import company.vk.education.siriusapp.BuildConfig
import company.vk.education.siriusapp.data.api.GeocoderAPI
import company.vk.education.siriusapp.data.api.GeocoderMapper
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val geocoderAPI: GeocoderAPI,
    private val geocoderMapper: GeocoderMapper
) : AddressRepository {
    override suspend fun getLocationOfAnAddress(address: String): Location {
        return geocoderMapper.map(
            geocoderAPI.convert(
                address,
                BuildConfig.GEOCODER_API_KEY
            )
        ).location
    }

    override suspend fun getAddressOfLocation(location: Location): String {
        return geocoderMapper.map(
            geocoderAPI.convert(
                "${location.latitude} ${location.longitude}",
                BuildConfig.GEOCODER_API_KEY
            )
        ).name
    }

}