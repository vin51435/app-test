package com.manasvia.android.core.di

import com.manasvia.android.data.analytics.LoggingAnalyticsTrackerImpl
import com.manasvia.android.data.repository.AuthRepositoryImpl
import com.manasvia.android.data.repository.DefaultWallpaperRepositoryImpl
import com.manasvia.android.domain.repository.AnalyticsTracker
import com.manasvia.android.domain.repository.AuthRepository
import com.manasvia.android.domain.repository.WallpaperRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module binding repository and telemetry implementations to their domain interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindWallpaperRepository(
        wallpaperRepositoryImpl: DefaultWallpaperRepositoryImpl
    ): WallpaperRepository

    @Binds
    @Singleton
    abstract fun bindAnalyticsTracker(
        analyticsTrackerImpl: LoggingAnalyticsTrackerImpl
    ): AnalyticsTracker
}
