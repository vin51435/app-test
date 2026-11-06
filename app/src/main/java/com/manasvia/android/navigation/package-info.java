/**
 * Central Navigation Routing and Graph Orchestration Layer.
 *
 * <h2>Architectural Purpose:</h2>
 * Implements the centralized {@link androidx.navigation.compose.NavHost} for Manasvia.
 * Integrates feature-specific destination builders (Splash, Onboarding, Gallery)
 * while keeping individual feature screens completely decoupled from navigation backstacks.
 *
 * <h2>Files in this package:</h2>
 * <ul>
 *   <li><b>{@code ManasviaNavHost.kt}</b>: Root Composable NavHost defining the app's
 *       navigation graph and route transitions with popUpTo backstack clearing.</li>
 * </ul>
 */
package com.manasvia.android.navigation;
