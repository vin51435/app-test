package com.manasvia.android.data.remote.api

import com.manasvia.android.core.common.AppConstants
import com.manasvia.android.data.remote.dto.ApiResponseDto
import com.manasvia.android.data.remote.dto.GuestAuthRequestDto
import com.manasvia.android.data.remote.dto.RefreshTokenRequestDto
import com.manasvia.android.data.remote.dto.TokenRefreshResponseDto
import com.manasvia.android.data.remote.dto.TokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Retrofit contract for authentication endpoints on the Spring Boot backend.
 */
interface AuthApiService {

    @Headers(AppConstants.HEADER_NO_AUTH_TAG)
    @POST("auth/guest")
    suspend fun authenticateGuest(
        @Body request: GuestAuthRequestDto
    ): Response<ApiResponseDto<TokenResponseDto>>

    @Headers(AppConstants.HEADER_NO_AUTH_TAG)
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequestDto
    ): Response<ApiResponseDto<TokenRefreshResponseDto>>
}
