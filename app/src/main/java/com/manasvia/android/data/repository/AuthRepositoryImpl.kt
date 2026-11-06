package com.manasvia.android.data.repository

import com.manasvia.android.core.common.Resource
import com.manasvia.android.core.datastore.UserPreferencesDataStore
import com.manasvia.android.core.di.IoDispatcher
import com.manasvia.android.data.remote.api.AuthApiService
import com.manasvia.android.data.remote.dto.GuestAuthRequestDto
import com.manasvia.android.data.remote.dto.RefreshTokenRequestDto
import com.manasvia.android.data.remote.mapper.toDomain
import com.manasvia.android.domain.model.AuthTokens
import com.manasvia.android.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete repository implementation managing identity sessions, local token caching,
 * and remote network exchanges.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val preferencesDataStore: UserPreferencesDataStore,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override fun getDeviceFingerprint(): Flow<String?> =
        preferencesDataStore.deviceFingerprintFlow

    override fun getAccessToken(): Flow<String?> =
        preferencesDataStore.accessTokenFlow

    override fun getRefreshToken(): Flow<String?> =
        preferencesDataStore.refreshTokenFlow

    override suspend fun hasValidSession(): Boolean = withContext(ioDispatcher) {
        val token = preferencesDataStore.accessTokenFlow.firstOrNull()
        !token.isNullOrBlank()
    }

    override suspend fun authenticateGuest(deviceFingerprint: String): Resource<AuthTokens> =
        withContext(ioDispatcher) {
            try {
                val request = GuestAuthRequestDto(deviceFingerprint = deviceFingerprint)
                val response = authApiService.authenticateGuest(request)

                if (response.isSuccessful && response.body()?.data != null) {
                    val tokenDto = response.body()!!.data!!
                    val domainTokens = tokenDto.toDomain()

                    // Persist credentials in DataStore
                    preferencesDataStore.saveTokens(
                        accessToken = domainTokens.accessToken,
                        refreshToken = domainTokens.refreshToken,
                        userId = domainTokens.userId,
                        role = domainTokens.role.name
                    )

                    Resource.Success(domainTokens)
                } else {
                    val errorMessage = response.body()?.message ?: "Guest authentication failed: HTTP ${response.code()}"
                    Resource.Error(errorMessage)
                }
            } catch (e: Exception) {
                Resource.Error(
                    message = e.localizedMessage ?: "Network error during guest authentication",
                    cause = e
                )
            }
        }

    override suspend fun refreshAccessToken(): Resource<String> = withContext(ioDispatcher) {
        try {
            val refreshToken = preferencesDataStore.refreshTokenFlow.firstOrNull()
                ?: return@withContext Resource.Error("No refresh token available")

            val response = authApiService.refreshToken(RefreshTokenRequestDto(refreshToken))
            if (response.isSuccessful && response.body()?.data != null) {
                val newAccessToken = response.body()!!.data!!.accessToken
                preferencesDataStore.updateAccessToken(newAccessToken)
                Resource.Success(newAccessToken)
            } else {
                Resource.Error(response.body()?.message ?: "Failed to refresh token")
            }
        } catch (e: Exception) {
            Resource.Error(
                message = e.localizedMessage ?: "Network error during token refresh",
                cause = e
            )
        }
    }

    override suspend fun saveDeviceFingerprint(fingerprint: String) = withContext(ioDispatcher) {
        preferencesDataStore.saveDeviceFingerprint(fingerprint)
    }

    override suspend fun clearSession() = withContext(ioDispatcher) {
        preferencesDataStore.clearSessionTokens()
    }
}
