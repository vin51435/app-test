package com.manasvia.android.domain.model

/**
 * Immutable domain representation of authentication credentials and active session state.
 */
data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val userId: String,
    val role: UserRole
)
