package com.manasvia.android.core.network

import com.manasvia.android.core.common.AppConstants
import com.manasvia.android.core.datastore.UserPreferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp Interceptor that automatically attaches "Authorization: Bearer <token>"
 * to all outgoing requests when an active session token is present.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val preferencesDataStore: UserPreferencesDataStore
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip adding auth header for endpoints explicitly marked with No-Authentication
        if (originalRequest.header(AppConstants.HEADER_NO_AUTHENTICATION) != null) {
            val cleanRequest = originalRequest.newBuilder()
                .removeHeader(AppConstants.HEADER_NO_AUTHENTICATION)
                .build()
            return chain.proceed(cleanRequest)
        }

        val token = runBlocking {
            preferencesDataStore.accessTokenFlow.firstOrNull()
        }

        val request = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}
