package com.manasvia.android

import android.app.Application
import com.manasvia.android.core.di.AppContainer

/**
 * Manasvia Application entry container.
 * Hosts the central AppContainer singleton for clean, robust dependency injection.
 */
class ManasviaApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
