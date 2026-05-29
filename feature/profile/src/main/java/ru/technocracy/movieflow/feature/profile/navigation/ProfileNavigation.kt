package ru.technocracy.movieflow.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.technocracy.movieflow.feature.profile.di.UserMovieListViewModelFactory
import ru.technocracy.movieflow.feature.profile.presentation.UserMovieListScreen

object UserMovieListRoute {
    const val ROUTE = "user_movie_list/{type}"
    fun createRoute(type: String) = "user_movie_list/$type"
}

fun NavGraphBuilder.userMovieListScreen(
    viewModelFactory: UserMovieListViewModelFactory,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    composable(
        route = UserMovieListRoute.ROUTE,
        arguments = listOf(navArgument("type") { type = NavType.StringType })
    ) { backStackEntry ->
        val type = backStackEntry.arguments?.getString("type").orEmpty()
        UserMovieListScreen(
            type = type,
            viewModelFactory = viewModelFactory.createWithType(type),
            onMovieClick = onMovieClick,
            onBack = onBack
        )
    }
}