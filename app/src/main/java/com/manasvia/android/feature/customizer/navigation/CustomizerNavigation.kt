package com.manasvia.android.feature.customizer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.manasvia.android.feature.customizer.CustomizerScreen

const val CUSTOMIZER_ROUTE = "customizer_route"

/**
 * Extension on [NavController] to navigate to the Customizer Studio destination.
 */
fun NavController.navigateToCustomizer(navOptions: NavOptions? = null) {
    navigate(CUSTOMIZER_ROUTE, navOptions)
}

/**
 * Registers the Customizer Studio destination in the navigation graph.
 */
fun NavGraphBuilder.customizerDestination(
    onBackClick: () -> Unit
) {
    composable(route = CUSTOMIZER_ROUTE) {
        CustomizerScreen(onBackClick = onBackClick)
    }
}
