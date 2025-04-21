package org.neopool.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.neopool.project.di.appModule
import org.neopool.project.poolreward.presentation.PoolNavigationRoute
import org.neopool.project.poolreward.presentation.poolScreen

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
            ) {
                val navigator = rememberNavController()

                NavHost(
                    navController = navigator,
                    startDestination = PoolNavigationRoute,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    poolScreen()
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}