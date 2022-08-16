package company.vk.education.siriusapp.di

import company.vk.education.siriusapp.data.TripsRepositoryImpl
import company.vk.education.siriusapp.domain.repository.TripsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideTripRepository(): TripsRepository = TripsRepositoryImpl()
}