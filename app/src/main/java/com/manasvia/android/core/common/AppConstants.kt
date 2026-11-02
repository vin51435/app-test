package com.manasvia.android.core.common

object AppConstants {
    // Android emulator loopback host to Spring Boot backend
    const val DEFAULT_BACKEND_BASE_URL = "http://192.168.0.104:8080/api/v1/"
    
    // Preferences DataStore Name
    const val DATASTORE_PREFERENCES_NAME = "manasvia_user_prefs"
    
    // Room Database Name
    const val ROOM_DATABASE_NAME = "manasvia_offline_database"

    // Network & Authentication Headers
    const val HEADER_NO_AUTHENTICATION = "No-Authentication"
    const val HEADER_NO_AUTH_TAG = "$HEADER_NO_AUTHENTICATION: true"
}
