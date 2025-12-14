package com.aryandi.requestapp.di

import com.aryandi.requestapp.data.MockRequestService
import com.aryandi.requestapp.data.RequestService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RequestServiceModule {
    @Provides
    @Singleton
    fun provideRequestService(): RequestService = MockRequestService()
}
