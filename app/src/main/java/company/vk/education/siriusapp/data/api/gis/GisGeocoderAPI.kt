package company.vk.education.siriusapp.data.api.gis

import retrofit2.http.GET
import retrofit2.http.Query

interface GisGeocoderAPI {
    @GET("items/geocode")
    suspend fun convert(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") apikey: String,
        @Query("fields") fields: String = "items.point"
    ): GisAddressResponse
}