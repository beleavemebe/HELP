package com.help.android.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.help.android.core.CurrentActivityProvider
import com.help.android.core.CurrentActivityProviderImpl
import com.help.android.data.*
import com.help.android.data.api.fake.FakeAddressRepository
import com.help.android.domain.repository.AddressRepository
import com.help.android.domain.repository.TripsRepository
import com.help.android.domain.service.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {
    @Binds
    @Singleton
    fun bindCurrentActivityProvider(
        impl: CurrentActivityProviderImpl
    ): CurrentActivityProvider

    @Binds
    @Singleton
    fun bindAuthService(
        impl: AuthServiceImpl
    ): AuthService

    @Binds
    @Singleton
    fun bindLocationService(
        impl: LocationServiceImpl
    ): LocationService

    @Binds
    @Singleton
    fun bindAddressRepository(
        impl: FakeAddressRepository
    ): AddressRepository

    @Binds
    @Singleton
    fun bindTripsRepository(
        impl: TripsRepositoryImpl
    ): TripsRepository

    companion object {
        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = Firebase.firestore
    }
}