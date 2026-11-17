package com.manasvia.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Authentication response payload received from POST /api/v1/auth/guest.
 */
@Serializable
data class TokenResponseDto(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String,
    @SerialName("tokenType") val tokenType: String? = "Bearer",
    @SerialName("expiresIn") val expiresIn: Long = 2592000L,
    @SerialName("userId") val userId: String,
    @SerialName("role") val role: String
)
