package com.help.android.di

import com.help.android.core.DtoMapper
import com.help.android.core.Mapper
import com.help.android.data.api.yandex.AddressResponse
import com.help.android.data.api.yandex.GeocoderMapper
import com.help.android.data.api.yandex.Response
import com.help.android.data.mapper.TripDtoMapper
import com.help.android.data.mapper.UserDtoMapper
import com.help.android.data.model.TripDto
import com.help.android.data.model.UserDto
import com.help.android.domain.model.TaxiService
import com.help.android.domain.model.TaxiVehicleClass
import com.help.android.domain.model.Trip
import com.help.android.domain.model.User
import com.help.android.ui.screens.main.bottomsheet.TaxiServiceToStringResMapper
import com.help.android.ui.screens.main.bottomsheet.TaxiVehicleClassToStringResMapper
import com.help.android.ui.screens.trip.mapper.TripScreenTitleToStringResMapper
import com.help.android.ui.screens.trip.model.TripScreenTitle
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
    ): DtoMapper<Trip, TripDto>

    @Binds
    @Singleton
    fun bindUserDtoMapper(
        impl: UserDtoMapper,
    ): DtoMapper<User, UserDto>

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
}