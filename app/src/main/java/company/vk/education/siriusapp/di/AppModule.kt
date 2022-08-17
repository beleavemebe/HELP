package company.vk.education.siriusapp.di

import company.vk.education.siriusapp.data.AddressRepositoryImpl
import company.vk.education.siriusapp.data.AuthServiceImpl
import company.vk.education.siriusapp.data.TripsRepositoryImpl
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideTripRepository(): TripsRepository = TripsRepositoryImpl()

    @Provides
    fun provideGeocoder(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository =
        addressRepositoryImpl

    @Provides
    fun provideAuth(): AuthService = AuthServiceImpl()
}