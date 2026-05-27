package ru.technocracy.movieflow.feature.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.technocracy.movieflow.feature.details.di.DetailsViewModelFactory
import ru.technocracy.movieflow.feature.details.presentation.DetailsScreen

object DetailsRoute {
    const val ROUTE = "details/{movieId}"
    fun createRoute(movieId: Int) = "details/$movieId"
}

fun NavGraphBuilder.detailsScreen(
    viewModelFactory: DetailsViewModelFactory,
    onBack: () -> Unit
) {
    composable(
        route = DetailsRoute.ROUTE,
        arguments = listOf(navArgument("movieId") { type = NavType.IntType })
    ) { backStackEntry ->
        val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
        DetailsScreen(
            movieId = movieId,
            viewModelFactory = viewModelFactory,
            onBack = onBack
        )
    }
}