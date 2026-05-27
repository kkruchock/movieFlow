package ru.technocracy.movieflow.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.technocracy.feature.feed.di.CatalogViewModelFactory
import ru.technocracy.feature.feed.presentation.CatalogScreen
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import ru.technocracy.movieflow.feature.auth.presentation.AuthScreen

// todo вынести отдельно sealed
object AppRoute {
    const val AUTH = "auth"
    const val CATALOG = "catalog"
}

@Composable
fun AppNavGraph(
    authViewModelFactory: AuthViewModelFactory,
    catalogViewModelFactory: CatalogViewModelFactory,
) {
    val navController: NavHostController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = AppRoute.AUTH
    ) {
        composable(AppRoute.AUTH) {
            AuthScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoute.CATALOG) {
                        popUpTo(AppRoute.AUTH) { inclusive = true }
                    }
                },
                viewModelFactory = authViewModelFactory
            )
        }
        composable(AppRoute.CATALOG) {
            CatalogScreen(viewModelFactory = catalogViewModelFactory)
        }
    }
}