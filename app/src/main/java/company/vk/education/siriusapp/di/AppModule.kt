package company.vk.education.siriusapp.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import company.vk.education.siriusapp.core.CurrentActivityProvider
import company.vk.education.siriusapp.core.CurrentActivityProviderImpl
import company.vk.education.siriusapp.data.*
import company.vk.education.siriusapp.data.db.AppDatabase
import company.vk.education.siriusapp.data.db.AppDatabase.Companion.DATABASE_NAME
import company.vk.education.siriusapp.domain.repository.AddressRepository
import company.vk.education.siriusapp.domain.repository.TripsRepository
import company.vk.education.siriusapp.data.FakeCurrentTripService
import company.vk.education.siriusapp.domain.service.*
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
        impl: AddressRepositoryImpl
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