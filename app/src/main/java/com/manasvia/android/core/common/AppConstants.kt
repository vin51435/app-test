package com.manasvia.android.core.common

object AppConstants {
    // Android emulator loopback host to Spring Boot backend
    const val DEFAULT_BACKEND_BASE_URL = "http://10.0.2.2:8080/api/v1/"
    
    // Preferences DataStore Name
    const val DATASTORE_PREFERENCES_NAME = "manasvia_user_prefs"
    
    // Room Database Name
    const val ROOM_DATABASE_NAME = "manasvia_offline_database"
}
