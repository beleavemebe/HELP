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
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.domain.service.CurrentTripService
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiServiceToStringResMapper
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiVehicleClassToStringResMapper
import company.vk.education.siriusapp.ui.screens.main.trip.TripScreenTitle
import company.vk.education.siriusapp.ui.screens.main.trip.TripScreenTitleToStringResMapper
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

    @Binds
    @Singleton
    fun bindTaxiServiceMapper(
        impl: TaxiServiceToStringResMapper,
    ): Mapper<TaxiService, Int>

    @Binds
    @Singleton
    fun bindTaxiVehicleClassMapper(
        impl: TaxiVehicleClassToStringResMapper,
    ): Mapper<TaxiVehicleClass, Int>

    @Binds
    @Singleton
    fun bindTripScreenTitleMapper(
        impl: TripScreenTitleToStringResMapper,
    ): Mapper<TripScreenTitle, Int>

    companion object {
        @Provides
        @Singleton
        fun provideCurrentTripStateService(
            @ApplicationContext context: Context,
        ): CurrentTripService = CurrentTripServiceImpl(context, CurrentTripStateMapper())
    }
}