package com.help.android.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.help.android.core.CurrentActivityProvider
import com.help.android.core.CurrentActivityProviderImpl
import com.help.android.data.AuthServiceImpl
import com.help.android.data.*
import com.help.android.data.db.AppDatabase
import com.help.android.data.db.AppDatabase.Companion.DATABASE_NAME
import com.help.android.domain.repository.AddressRepository
import com.help.android.domain.repository.TripsRepository
import com.help.android.data.FakeCurrentTripService
import com.help.android.data.ScheduledTripsServiceImpl
import com.help.android.data.TripsRepositoryImpl
import com.help.android.data.api.fake.FakeAddressRepository
import com.help.android.domain.service.*
import com.help.android.domain.service.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun bindScheduledTripsService(
        impl: ScheduledTripsServiceImpl
    ): ScheduledTripsService

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
//        @Provides
//        @Singleton
//        fun provideCurrentTripStateService(
//            @ApplicationContext context: Context,
//        ): CurrentTripService = CurrentTripServiceImpl(context, CurrentTripStateMapper())

        @Provides
        @Singleton
        fun provideCurrentTripStateService(
        ): CurrentTripService = FakeCurrentTripService()

        @Provides
        @Singleton
        fun provideFirestore(): FirebaseFirestore = Firebase.firestore

        @Provides
        @Singleton
        fun provideAppDatabase(
            @ApplicationContext context: Context,
        ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

//        @Provides
//        @Singleton
//        fun provideScheduledTripsDao(
//            appDatabase: AppDatabase
//        ): ScheduledTripsDao = appDatabase.scheduledTripsDao()
    }
}