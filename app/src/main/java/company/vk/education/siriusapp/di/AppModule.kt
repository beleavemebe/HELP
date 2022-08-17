package company.vk.education.siriusapp.di

import android.content.Context
import company.vk.education.siriusapp.domain.repository.AddressRepositoryImpl
import company.vk.education.siriusapp.data.TripsRepositoryImpl
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.domain.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideTripRepository(): TripsRepository = TripsRepositoryImpl()

    @Provides
    fun provideAuthService(@ApplicationContext context: Context): AuthService = TODO()

    @Provides
    fun provideGeocoder(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository = addressRepositoryImpl
}