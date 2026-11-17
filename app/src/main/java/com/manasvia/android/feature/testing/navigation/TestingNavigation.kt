package com.manasvia.android.feature.testing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.testing.TestingSplashScreen
import kotlinx.serialization.Serializable

/**
 * Type-Safe Navigation Route for Testing / Playground Splash Screen.
 */
@Serializable
object TestingSplashRoute

/**
 * Extension on [NavController] to navigate to the Testing / Playground Splash destination.
 */
fun NavController.navigateToTestingSplash(navOptions: NavOptions? = null) {
    navigate(route = TestingSplashRoute, navOptions = navOptions)
}

/**
 * Registers the Testing Splash Screen destination in the navigation graph using type-safe routing.
 */
fun NavGraphBuilder.testingSplashDestination(
    onNavigateToSplash: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToCustomizer: () -> Unit
) {
    composable<TestingSplashRoute> {
        TestingSplashScreen(
            onNavigateToSplash = onNavigateToSplash,
            onNavigateToOnboarding = onNavigateToOnboarding,
            onNavigateToGallery = onNavigateToGallery,
            onNavigateToCustomizer = onNavigateToCustomizer
        )
    }
}
