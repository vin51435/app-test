package com.manasvia.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Manasvia Application entry container.
 * Annotated with @HiltAndroidApp to trigger Hilt's root dependency graph compilation.
 */
@HiltAndroidApp
class ManasviaApp : Application()
