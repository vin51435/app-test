package com.manasvia.android.feature.onboarding

/**
 * UI State representation for the initial session initialization and guest verification.
 */
sealed interface SplashUiState {
    data object Loading : SplashUiState
    data class Authenticated(val userId: String, val role: String) : SplashUiState
    data class Error(val message: String) : SplashUiState
}
