package company.vk.education.siriusapp.di

import company.vk.education.siriusapp.core.CurrentActivityProvider
import company.vk.education.siriusapp.core.CurrentActivityProviderImpl
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTripRepository(): TripsRepository = TripsRepositoryImpl()

    @Provides
    @Singleton
    fun provideGeocoder(addressRepository: AddressRepositoryImpl): AddressRepository =
        addressRepository

    @Provides
    @Singleton
    fun provideCurrentActivityProvider(currentActivityProvider: CurrentActivityProviderImpl): CurrentActivityProvider =
        currentActivityProvider

    @Provides
    @Singleton
    fun provideAuthService(authServiceImpl: AuthServiceImpl): AuthService = authServiceImpl
}