package com.help.android.data.api.yandex

import com.help.android.core.Mapper
import com.help.android.domain.model.Location
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