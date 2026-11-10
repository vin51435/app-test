package com.manasvia.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Manasvia Application entry point.
 * Annotated with @HiltAndroidApp to initialize Hilt dependency injection,
 * generating the application-level component that acts as the root of the dependency tree.
 */
@HiltAndroidApp
class ManasviaApp : Application()
