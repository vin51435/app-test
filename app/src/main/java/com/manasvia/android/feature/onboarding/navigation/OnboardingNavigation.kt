package com.manasvia.android.feature.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.onboarding.OnboardingScreen
import com.manasvia.android.feature.onboarding.SplashScreen
import kotlinx.serialization.Serializable

/**
 * Type-Safe Navigation Route for Splash Screen.
 */
@Serializable
object SplashRoute

/**
 * Type-Safe Navigation Route for Onboarding Screen.
 */
@Serializable
object OnboardingRoute

/**
 * Extension on [NavController] to navigate to the Splash destination.
 */
fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    navigate(route = SplashRoute, navOptions = navOptions)
}

/**
 * Extension on [NavController] to navigate to the Onboarding welcome destination.
 */
fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    navigate(route = OnboardingRoute, navOptions = navOptions)
}

/**
 * Registers the Splash destination in the navigation graph using type-safe routing.
 *
 * @param onSessionAuthenticated Callback triggered when guest identity is verified and ready.
 * @param onNavigateToTesting Callback to open the testing playground splash.
 */
fun NavGraphBuilder.splashDestination(
    onSessionAuthenticated: () -> Unit,
    onNavigateToTesting: () -> Unit = {}
) {
    composable<SplashRoute> {
        SplashScreen(
            onSessionAuthenticated = onSessionAuthenticated,
            onNavigateToTesting = onNavigateToTesting
        )
    }
}

/**
 * Registers the Onboarding welcome destination in the navigation graph using type-safe routing.
 *
 * @param onFinishOnboarding Callback triggered when onboarding is completed or skipped.
 */
fun NavGraphBuilder.onboardingDestination(
    onFinishOnboarding: () -> Unit
) {
    composable<OnboardingRoute> {
        OnboardingScreen(
            onFinishOnboarding = onFinishOnboarding
        )
    }
}
