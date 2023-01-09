package com.help.android.data.api.yandex

import com.google.gson.annotations.SerializedName
import com.help.android.domain.model.Location

class AddressResponse(val name: String, val location: Location) {
    companion object {
        val EMPTY_ADDRESS = AddressResponse("UNKNOWN", Location.LOCATION_UNKNOWN)
    }
}

class Response(
    @SerializedName("response")
    val response: ResponseInner
)

class ResponseInner(
    @SerializedName("GeoObjectCollection")
    val collection: ObjectCollection
)

class ObjectCollection(
    @SerializedName("featureMember")
    val members: List<Member>
)

class Member(
    @SerializedName("GeoObject")
    val geoObject: GeoObject
)

class GeoObject(
    @SerializedName("name")
    val name: String,
    @SerializedName("Point")
    val point: Point
)

class Point(
    @SerializedName("pos")
    val pos: String
)