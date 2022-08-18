package company.vk.education.siriusapp.di

import company.vk.education.siriusapp.data.AuthServiceImpl
import company.vk.education.siriusapp.domain.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthModule {
    @Provides
    fun provideAuth(authServiceImpl: AuthServiceImpl): AuthService = authServiceImpl
}