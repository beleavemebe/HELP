package company.vk.education.siriusapp.data.api.yandex

import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.Location
import javax.inject.Inject

class GeocoderMapper @Inject constructor() : Mapper<Response, AddressResponse> {
    override fun map(arg: Response): AddressResponse {
        return arg.response.collection.members.firstOrNull()
            ?.geoObject
            ?.run {
                val (lon, lat) = point.pos.split(" ").map { it.toDouble() }
                AddressResponse(name, Location(lat, lon))
            }
            ?: AddressResponse.EMPTY_ADDRESS
    }
}