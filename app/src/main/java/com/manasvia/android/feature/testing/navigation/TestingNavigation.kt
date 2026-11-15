package com.manasvia.android.feature.testing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.testing.TestingSplashScreen

const val TESTING_SPLASH_ROUTE = "testing_splash_route"

/**
 * Extension on [NavController] to navigate to the Testing / Playground Splash destination.
 */
fun NavController.navigateToTestingSplash(navOptions: NavOptions? = null) {
    navigate(TESTING_SPLASH_ROUTE, navOptions)
}

/**
 * Registers the Testing Splash Screen destination in the navigation graph.
 */
fun NavGraphBuilder.testingSplashDestination(
    onNavigateToSplash: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToCustomizer: () -> Unit
) {
    composable(route = TESTING_SPLASH_ROUTE) {
        TestingSplashScreen(
            onNavigateToSplash = onNavigateToSplash,
            onNavigateToOnboarding = onNavigateToOnboarding,
            onNavigateToGallery = onNavigateToGallery,
            onNavigateToCustomizer = onNavigateToCustomizer
        )
    }
}
