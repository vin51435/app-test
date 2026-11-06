package com.manasvia.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.manasvia.android.feature.gallery.navigation.galleryDestination
import com.manasvia.android.feature.gallery.navigation.navigateToGallery
import com.manasvia.android.feature.onboarding.navigation.ONBOARDING_ROUTE
import com.manasvia.android.feature.onboarding.navigation.SPLASH_ROUTE
import com.manasvia.android.feature.onboarding.navigation.navigateToOnboarding
import com.manasvia.android.feature.onboarding.navigation.onboardingDestination
import com.manasvia.android.feature.onboarding.navigation.splashDestination

/**
 * Top-level Navigation Host for Manasvia.
 * Coordinates feature destinations and transitions across the entire application.
 */
@Composable
fun ManasviaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = SPLASH_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. Splash / Session Initialization Destination
        splashDestination(
            onSessionAuthenticated = {
                navController.navigateToOnboarding(
                    navOptions {
                        popUpTo(SPLASH_ROUTE) { inclusive = true }
                    }
                )
            }
        )

        // 2. Onboarding / Value Proposition Destination
        onboardingDestination(
            onFinishOnboarding = {
                navController.navigateToGallery(
                    navOptions {
                        popUpTo(ONBOARDING_ROUTE) { inclusive = true }
                    }
                )
            }
        )

        // 3. Main Home Hub & Theme Gallery Destination
        galleryDestination()
    }
}
