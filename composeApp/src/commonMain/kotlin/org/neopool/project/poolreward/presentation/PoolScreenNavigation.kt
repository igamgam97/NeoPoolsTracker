package org.neopool.project.poolreward.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val PoolNavigationRoute = "pool"

fun NavGraphBuilder.poolScreen() {
    composable(
        route = PoolNavigationRoute,
    ) {
        PoolRoute()
    }
}