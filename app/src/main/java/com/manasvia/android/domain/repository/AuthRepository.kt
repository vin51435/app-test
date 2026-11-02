package com.manasvia.android.domain.repository

import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.model.AuthTokens
import kotlinx.coroutines.flow.Flow

/**
 * Domain boundary contract for device identity, guest authentication, and token lifecycle management.
 */
interface AuthRepository {

    /**
     * Observes the locally stored device hardware fingerprint / UUID.
     */
    fun getDeviceFingerprint(): Flow<String?>

    /**
     * Observes the active JWT access token.
     */
    fun getAccessToken(): Flow<String?>

    /**
     * Observes the active session refresh token.
     */
    fun getRefreshToken(): Flow<String?>

    /**
     * Checks if the device currently possesses a non-empty access token.
     */
    suspend fun hasValidSession(): Boolean

    /**
     * Executes frictionless 0-login guest authentication against backend POST /api/v1/auth/guest.
     * Persists the resulting tokens on success.
     */
    suspend fun authenticateGuest(deviceFingerprint: String): Resource<AuthTokens>

    /**
     * Refreshes the expired access token using the stored refresh token.
     */
    suspend fun refreshAccessToken(): Resource<String>

    /**
     * Saves or initializes the persistent device fingerprint.
     */
    suspend fun saveDeviceFingerprint(fingerprint: String)

    /**
     * Clears all session credentials from local storage.
     */
    suspend fun clearSession()
}
