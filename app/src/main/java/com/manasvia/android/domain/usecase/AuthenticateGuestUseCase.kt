package com.manasvia.android.domain.usecase

import com.manasvia.android.core.common.Resource
import com.manasvia.android.domain.model.AuthTokens
import com.manasvia.android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

/**
 * UseCase implementing Workflow 1: Frictionless 0-Login App Launch & Guest Session.
 *
 * Guarantees that within 2 seconds of the first app launch, a persistent device identity
 * is established with the Spring Boot backend and Bearer tokens are stored without user intervention.
 */
class AuthenticateGuestUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(): Resource<AuthTokens> {
        // 1. If an active session already exists, skip remote registration
        if (authRepository.hasValidSession()) {
            val accessToken = authRepository.getAccessToken().firstOrNull()
            val refreshToken = authRepository.getRefreshToken().firstOrNull()
            if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                return Resource.Success(
                    AuthTokens(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        expiresIn = 0L,
                        userId = "",
                        role = com.manasvia.android.domain.model.UserRole.GUEST
                    )
                )
            }
        }

        // 2. Retrieve or generate unique device fingerprint
        var fingerprint = authRepository.getDeviceFingerprint().firstOrNull()
        if (fingerprint.isNullOrBlank()) {
            fingerprint = "manasvia-android-" + UUID.randomUUID().toString()
            authRepository.saveDeviceFingerprint(fingerprint)
        }

        // 3. Authenticate guest session with backend
        return authRepository.authenticateGuest(fingerprint)
    }
}
