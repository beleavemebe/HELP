package company.vk.education.siriusapp.data.api

import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.Location
import javax.inject.Inject

class GeocoderMapper @Inject constructor() : Mapper<Response, AddressResponse> {
    override fun map(arg: Response): AddressResponse =
        with(arg.response.collection.members[0].geoObject) {
            val (lon, lat) = point.pos.split(" ").map { it.toDouble() }
            return AddressResponse(name, Location(lat, lon))
        }
}