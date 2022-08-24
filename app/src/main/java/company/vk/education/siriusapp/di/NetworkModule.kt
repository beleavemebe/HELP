package company.vk.education.siriusapp.di

import company.vk.education.siriusapp.data.GEOCODER_URL
import company.vk.education.siriusapp.data.api.GeocoderAPI
import company.vk.education.siriusapp.ui.utils.log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttp() : OkHttpClient {
        return OkHttpClient.Builder()
//            .addInterceptor(
//                HttpLoggingInterceptor(::log).apply {
//                    level = HttpLoggingInterceptor.Level.BODY
//                }
//            )
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @GeoQualifier
    fun provideGeoRetrofit(client: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(GEOCODER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideApiService(@GeoQualifier retrofit: Retrofit) : GeocoderAPI {
        return retrofit.create(GeocoderAPI::class.java)
    }
}