package company.vk.education.siriusapp.data

import company.vk.education.siriusapp.BuildConfig
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.data.api.yandex.AddressResponse
import company.vk.education.siriusapp.data.api.yandex.GeocoderAPI
import company.vk.education.siriusapp.data.api.yandex.Response
import company.vk.education.siriusapp.domain.model.Location
import company.vk.education.siriusapp.domain.repository.AddressRepository
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