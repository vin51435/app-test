package com.manasvia.android.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Payload sent to POST /api/v1/auth/refresh to rotate an expired access token.
 */
data class RefreshTokenRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)
