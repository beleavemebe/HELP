package company.vk.education.siriusapp.data.api.yandex

import retrofit2.http.GET
import retrofit2.http.Query

interface GeocoderAPI {
    @GET("/1.x/")
    suspend fun convert(
        @Query("geocode") geocode: String,
        @Query("apikey") apikey: String,
        @Query("format") format: String = "json",
        @Query("result") result: Int = 1
    ): Response
}