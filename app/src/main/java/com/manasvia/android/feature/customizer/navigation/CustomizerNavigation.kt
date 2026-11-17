package com.manasvia.android.feature.customizer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.customizer.CustomizerScreen
import kotlinx.serialization.Serializable

/**
 * Type-Safe Navigation Route for Customizer Studio Screen.
 */
@Serializable
object CustomizerRoute

/**
 * Extension on [NavController] to navigate to the Customizer Studio destination.
 */
fun NavController.navigateToCustomizer(navOptions: NavOptions? = null) {
    navigate(route = CustomizerRoute, navOptions = navOptions)
}

/**
 * Registers the Customizer Studio destination in the navigation graph using type-safe routing.
 */
fun NavGraphBuilder.customizerDestination(
    onBackClick: () -> Unit
) {
    composable<CustomizerRoute> {
        CustomizerScreen(onBackClick = onBackClick)
    }
}
