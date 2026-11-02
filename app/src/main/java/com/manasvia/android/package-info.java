/**
 * Root Application Package for the Manasvia Native Android Client.
 *
 * <h2>Architectural Purpose:</h2>
 * Coordinates top-level Android lifecycle initialization, application singletons,
 * and the primary Single-Activity Jetpack Compose host.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code ManasviaApp.kt}</b>: Custom {@link android.app.Application} instance.
 *       Initializes and provides the root {@link com.manasvia.android.core.di.AppContainer}
 *       singleton on process creation.</li>
 *   <li><b>{@code MainActivity.kt}</b>: Primary {@link androidx.activity.ComponentActivity}
 *       entry point. Configures edge-to-edge window insets, hosts the Material 3 Compose
 *       theme, and observes {@link com.manasvia.android.feature.onboarding.SplashViewModel}
 *       to orchestrate the frictionless guest onboarding flow.</li>
 * </ul>
 */
package com.manasvia.android;
