package com.manasvia.android.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Authentication response payload received from POST /api/v1/auth/guest.
 */
data class TokenResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("tokenType") val tokenType: String? = "Bearer",
    @SerializedName("expiresIn") val expiresIn: Long = 2592000L,
    @SerializedName("userId") val userId: String,
    @SerializedName("role") val role: String
)
