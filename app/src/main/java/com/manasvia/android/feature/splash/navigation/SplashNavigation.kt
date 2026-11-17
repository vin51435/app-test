package com.manasvia.android.feature.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.splash.SplashScreen
import kotlinx.serialization.Serializable

/**
 * Type-Safe Navigation Route for Splash Screen.
 */
@Serializable
object SplashRoute

/**
 * Extension on [NavController] to navigate to the Splash destination.
 */
fun NavController.navigateToSplash(navOptions: NavOptions? = null) {
    navigate(route = SplashRoute, navOptions = navOptions)
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
