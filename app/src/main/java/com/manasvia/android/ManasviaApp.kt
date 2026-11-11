package com.manasvia.android

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.manasvia.android.worker.WallpaperWorkScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Manasvia Application entry point.
 * Annotated with @HiltAndroidApp to initialize Hilt dependency injection,
 * generating the application-level component that acts as the root of the dependency tree.
 * Implements [Configuration.Provider] to wire [HiltWorkerFactory] into Android WorkManager.
 */
@HiltAndroidApp
class ManasviaApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var wallpaperWorkScheduler: WallpaperWorkScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Ensure the daily morning 6:00 AM wallpaper refresh is scheduled
        wallpaperWorkScheduler.scheduleDailyMorningWorker()
    }
}
