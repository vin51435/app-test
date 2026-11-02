package com.manasvia.android.core.network

import com.google.gson.Gson
import com.manasvia.android.core.common.AppConstants
import com.manasvia.android.core.datastore.UserPreferencesDataStore
import com.manasvia.android.data.remote.dto.ApiResponseDto
import com.manasvia.android.data.remote.dto.RefreshTokenRequestDto
import com.manasvia.android.data.remote.dto.TokenRefreshResponseDto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles HTTP 401 Unauthorized responses by rotating access tokens via the stored refresh token
 * and transparently retrying the failed request.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val preferencesDataStore: UserPreferencesDataStore,
    private val gson: Gson
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Prevent infinite loops if refresh fails or endpoint itself is 401
        if (responseCount(response) >= 3) {
            return null
        }

        val refreshToken = runBlocking {
            preferencesDataStore.refreshTokenFlow.firstOrNull()
        } ?: return null

        synchronized(this) {
            val currentToken = runBlocking { preferencesDataStore.accessTokenFlow.firstOrNull() }
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            // If another thread already refreshed the token, retry with updated token immediately
            if (!currentToken.isNullOrBlank() && currentToken != requestToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            // Perform synchronous refresh HTTP call
            val refreshClient = OkHttpClient.Builder().build()
            val requestBody = gson.toJson(RefreshTokenRequestDto(refreshToken))
                .toRequestBody("application/json".toMediaType())

            val refreshRequest = Request.Builder()
                .url(AppConstants.DEFAULT_BACKEND_BASE_URL + "auth/refresh")
                .post(requestBody)
                .build()

            try {
                val refreshResponse = refreshClient.newCall(refreshRequest).execute()
                if (refreshResponse.isSuccessful) {
                    val responseBody = refreshResponse.body?.string()
                    val type = object : com.google.gson.reflect.TypeToken<ApiResponseDto<TokenRefreshResponseDto>>() {}.type
                    val apiResponse: ApiResponseDto<TokenRefreshResponseDto>? = gson.fromJson(responseBody, type)

                    val newAccessToken = apiResponse?.data?.accessToken
                    if (!newAccessToken.isNullOrBlank()) {
                        runBlocking {
                            preferencesDataStore.updateAccessToken(newAccessToken)
                        }
                        return response.request.newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                    }
                } else {
                    // Refresh token is revoked or expired: clear local session
                    runBlocking {
                        preferencesDataStore.clearSessionTokens()
                    }
                }
            } catch (e: Exception) {
                return null
            }
        }

        return null
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
