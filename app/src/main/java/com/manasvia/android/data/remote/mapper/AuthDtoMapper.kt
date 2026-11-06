package com.manasvia.android.data.remote.mapper

import com.manasvia.android.data.remote.dto.TokenResponseDto
import com.manasvia.android.domain.model.AuthTokens
import com.manasvia.android.domain.model.UserRole

/**
 * Extension mapper converting raw network DTOs to clean immutable domain entities.
 */
fun TokenResponseDto.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        tokenType = tokenType ?: "Bearer",
        expiresIn = expiresIn,
        userId = userId,
        role = UserRole.fromString(role)
    )
}
