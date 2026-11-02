package com.manasvia.android.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Payload received after rotating an access token.
 */
data class TokenRefreshResponseDto(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("tokenType") val tokenType: String? = "Bearer",
    @SerializedName("expiresIn") val expiresIn: Long = 2592000L
)
