/**
 * Frictionless Onboarding & Soul Tuning Questionnaire Feature.
 *
 * <h2>Architectural Purpose:</h2>
 * Implements Workflow 1: Frictionless first-launch session check, guest registration,
 * brand value proposition showcase, and the optional/deferred 6-step questionnaire pager.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code SplashScreen.kt}</b>: Observes {@link SplashViewModel} and drives
 *       session verification before transitioning through the navigation graph.</li>
 *   <li><b>{@code OnboardingScreen.kt}</b>: Aesthetic onboarding screen showcasing
 *       Manasvia's core value pillars with primary and secondary navigation actions.</li>
 *   <li><b>{@code SplashUiState.kt}</b>: Sealed interface representing UI states:
 *       {@code Loading}, {@code Authenticated(userId, role)}, and {@code Error(message)}.</li>
 *   <li><b>{@code SplashViewModel.kt}</b>: ViewModel orchestrating first-launch guest
 *       registration via {@link com.manasvia.android.domain.usecase.AuthenticateGuestUseCase}.</li>
 * </ul>
 */
package com.manasvia.android.feature.onboarding;
