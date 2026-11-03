/**
 * Frictionless Onboarding & Soul Tuning Questionnaire Feature.
 *
 * <h2>Architectural Purpose:</h2>
 * Implements Workflow 1: Frictionless first-launch session check, guest registration,
 * and the optional/deferred 6-step horizontal questionnaire pager (life season, stress signature,
 * wisdom lens).
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code SplashUiState.kt}</b>: Sealed interface representing UI states:
 *       {@code Loading}, {@code Authenticated(userId, role)}, and {@code Error(message)}.</li>
 *   <li><b>{@code SplashViewModel.kt}</b>: ViewModel orchestrating the first-launch guest
 *       registration via {@link com.manasvia.android.domain.usecase.AuthenticateGuestUseCase}.
 *       Provides {@code SplashViewModel.Factory} to bind cleanly without bytecode injection.</li>
 *   <li><b>{@code SplashScreen.kt}</b>: Declarative Jetpack Compose UI rendering the splash branding,
 *       loading indicator, authenticated guest confirmation, and error retry state.</li>
 * </ul>
 */
package com.manasvia.android.feature.onboarding;
