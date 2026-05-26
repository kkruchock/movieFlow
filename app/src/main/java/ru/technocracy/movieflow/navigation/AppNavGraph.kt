package ru.technocracy.movieflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.technocracy.feature.feed.presentation.HomeScreen
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import ru.technocracy.movieflow.feature.auth.presentation.AuthScreen

// todo вынести отдельно sealed
object AppRoute {
    const val AUTH = "auth"
    const val HOME = "home"
}

@Composable
fun AppNavGraph(
    viewModelFactory: AuthViewModelFactory
) {
    val navController: NavHostController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = AppRoute.AUTH
    ) {
        composable(AppRoute.AUTH) {
            AuthScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoute.HOME) {
                        popUpTo(AppRoute.AUTH) { inclusive = true }
                    }
                },
                viewModelFactory = viewModelFactory
            )
        }
        composable(AppRoute.HOME) {
            HomeScreen()
        }
    }
}