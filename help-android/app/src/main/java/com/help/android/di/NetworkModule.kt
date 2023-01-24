package com.help.android.di

import com.help.android.data.GIS_GEOCODER_URL
import com.help.android.data.api.ApiService
import com.help.android.data.YANDEX_GEOCODER_URL
import com.help.android.data.api.yandex.GeocoderAPI
import com.help.android.data.api.gis.GisGeocoderAPI
import com.help.android.ui.utils.log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor(::log).apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    fun provideBaseRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideGeoRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideApi(baseRetrofit: Retrofit): ApiService {
        return baseRetrofit.newBuilder()
            .baseUrl("http://192.168.0.111:8080")
            .build()
            .create()
    }

    @Provides
    fun provideYandexApiService(baseRetrofit: Retrofit): GeocoderAPI {
        return baseRetrofit.newBuilder()
            .baseUrl(YANDEX_GEOCODER_URL)
            .build()
            .create(GeocoderAPI::class.java)
    }

    @Provides
    fun provideGisApiService(baseRetrofit: Retrofit): GisGeocoderAPI {
        return baseRetrofit.newBuilder()
            .baseUrl(GIS_GEOCODER_URL)
            .build()
            .create(GisGeocoderAPI::class.java)
    }
}