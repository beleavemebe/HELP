package company.vk.education.siriusapp.di

import android.content.Context
import company.vk.education.siriusapp.core.BiMapper
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.data.CurrentTripServiceImpl
import company.vk.education.siriusapp.data.api.AddressResponse
import company.vk.education.siriusapp.data.api.GeocoderMapper
import company.vk.education.siriusapp.data.api.Response
import company.vk.education.siriusapp.data.mapper.CurrentTripStateMapper
import company.vk.education.siriusapp.data.mapper.TripDtoMapper
import company.vk.education.siriusapp.data.mapper.UserDtoMapper
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.data.model.UserDto
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.domain.service.CurrentTripService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MapperBindingsModule {
    @Binds
    @Singleton
    fun bindGeocoderMapper(
        impl: GeocoderMapper,
    ): Mapper<Response, AddressResponse>

    @Binds
    @Singleton
    fun bindTripDtoMapper(
        impl: TripDtoMapper,
    ): BiMapper<Trip, TripDto>

    @Binds
    @Singleton
    fun bindUserDtoMapper(
        impl: UserDtoMapper,
    ): BiMapper<User, UserDto>

    companion object {
        @Provides
        @Singleton
        fun provideCurrentTripStateService(
            @ApplicationContext context: Context,
        ): CurrentTripService = CurrentTripServiceImpl(context, CurrentTripStateMapper())
    }
}