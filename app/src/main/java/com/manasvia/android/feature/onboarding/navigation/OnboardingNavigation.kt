package com.manasvia.android.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.onboarding.OnboardingScreen
import com.manasvia.android.feature.onboarding.SplashScreen

const val SPLASH_ROUTE = "splash_route"
const val ONBOARDING_ROUTE = "onboarding_route"

/**
 * Extension on [NavController] to navigate to the Splash destination.
 */
fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    navigate(SPLASH_ROUTE, navOptions)
}

/**
 * Extension on [NavController] to navigate to the Onboarding welcome destination.
 */
fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(ONBOARDING_ROUTE, navOptions)
}

/**
 * Registers the Splash destination in the navigation graph.
 *
 * @param onSessionAuthenticated Callback triggered when guest identity is verified and ready.
 * @param onNavigateToTesting Callback to open the testing playground splash.
 */
fun NavGraphBuilder.splashDestination(
    onSessionAuthenticated: () -> Unit,
    onNavigateToTesting: () -> Unit = {}
) {
    composable(route = SPLASH_ROUTE) {
        SplashScreen(
            onSessionAuthenticated = onSessionAuthenticated,
            onNavigateToTesting = onNavigateToTesting
        )
    }
}

/**
 * Registers the Onboarding welcome & soul tuning destination in the navigation graph.
 *
 * @param onFinishOnboarding Callback triggered when onboarding is completed or skipped.
 */
fun NavGraphBuilder.onboardingDestination(
    onFinishOnboarding: () -> Unit
) {
    composable(route = ONBOARDING_ROUTE) {
        OnboardingScreen(
            onFinishOnboarding = onFinishOnboarding
        )
    }
}
