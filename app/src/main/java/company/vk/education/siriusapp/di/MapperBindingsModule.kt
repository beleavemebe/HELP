package company.vk.education.siriusapp.di

import androidx.compose.ui.graphics.Color
import company.vk.education.siriusapp.core.DtoMapper
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.data.api.yandex.AddressResponse
import company.vk.education.siriusapp.data.api.yandex.GeocoderMapper
import company.vk.education.siriusapp.data.api.yandex.Response
import company.vk.education.siriusapp.data.mapper.TripDtoMapper
import company.vk.education.siriusapp.data.mapper.UserDtoMapper
import company.vk.education.siriusapp.data.model.TripDto
import company.vk.education.siriusapp.data.model.UserDto
import company.vk.education.siriusapp.domain.model.TaxiService
import company.vk.education.siriusapp.domain.model.TaxiVehicleClass
import company.vk.education.siriusapp.domain.model.Trip
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiServiceToStringResMapper
import company.vk.education.siriusapp.ui.screens.main.bottomsheet.TaxiVehicleClassToStringResMapper
import company.vk.education.siriusapp.ui.screens.trip.mapper.TripScreenTitleToStringResMapper
import company.vk.education.siriusapp.ui.library.trips.TripItemButtonState
import company.vk.education.siriusapp.ui.screens.trip.mapper.TripItemButtonStateToColorMapper
import company.vk.education.siriusapp.ui.screens.trip.mapper.TripItemButtonStateToStringResMapper
import company.vk.education.siriusapp.ui.screens.trip.model.TripScreenTitle
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

    @Binds
    @Singleton
    fun bindTripItemButtonStateTextMapper(
        impl: TripItemButtonStateToStringResMapper,
    ): Mapper<TripItemButtonState, Int>

    @Binds
    @Singleton
    fun bindTripItemButtonStateColorMapper(
        impl: TripItemButtonStateToColorMapper,
    ): Mapper<TripItemButtonState, Color>
}