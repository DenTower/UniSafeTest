package org.example.project.presentation.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.presentation.screens.LoginScreen
import org.example.project.presentation.screens.MainScreen
import org.example.project.presentation.viewmodel.ShoppingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel: ShoppingViewModel = koinViewModel()

    NavHost(navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToMain = { navController.navigate(Routes.MAIN) },
                viewModel = viewModel
            )
        }

        composable(Routes.MAIN) {
            MainScreen(
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                viewModel = viewModel
            )
        }
    }
}