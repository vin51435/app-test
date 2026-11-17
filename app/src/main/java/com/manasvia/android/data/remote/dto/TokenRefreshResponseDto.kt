package com.manasvia.android.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Payload received after rotating an access token.
 */
@Serializable
data class TokenRefreshResponseDto(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("tokenType") val tokenType: String? = "Bearer",
    @SerialName("expiresIn") val expiresIn: Long = 2592000L
)
