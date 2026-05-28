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
import ru.technocracy.movieflow.feature.collections.di.CollectionsViewModelFactory
import ru.technocracy.movieflow.feature.collections.navigation.CollectionEditRoute
import ru.technocracy.movieflow.feature.collections.navigation.collectionEditScreen
import ru.technocracy.movieflow.feature.details.di.DetailsViewModelFactory
import ru.technocracy.movieflow.feature.details.navigation.DetailsRoute
import ru.technocracy.movieflow.feature.details.navigation.detailsScreen
import ru.technocracy.movieflow.feature.search.di.SearchViewModelFactory
import ru.technocracy.movieflow.feature.search.navigation.SearchRoute
import ru.technocracy.movieflow.feature.search.navigation.searchPickScreen
import ru.technocracy.movieflow.feature.search.navigation.searchScreen

object AppRoute {
    const val AUTH = "auth"
    const val CATALOG = "catalog"
}

@Composable
fun AppNavGraph(
    authViewModelFactory: AuthViewModelFactory,
    catalogViewModelFactory: CatalogViewModelFactory,
    detailsViewModelFactory: DetailsViewModelFactory,
    searchViewModelFactory: SearchViewModelFactory,
    collectionsViewModelFactory: CollectionsViewModelFactory,
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
            CatalogScreen(
                viewModelFactory = catalogViewModelFactory,
                onMovieClick = { movieId -> navController.navigate(DetailsRoute.createRoute(movieId)) },
                onNavigateToSearch = { navController.navigate(SearchRoute.ROUTE) }
            )
        }

        detailsScreen(
            viewModelFactory = detailsViewModelFactory,
            onBack = { navController.navigateUp() }
        )

        searchScreen(
            viewModelFactory = searchViewModelFactory,
            onMovieClick = { movieId -> navController.navigate(DetailsRoute.createRoute(movieId)) },
            onBack = { navController.navigateUp() }
        )

        collectionEditScreen(
            viewModelFactory = collectionsViewModelFactory,
            onBack = { navController.navigateUp() },
            onPickMovie = { navController.navigate("search_pick") }
        )

        searchPickScreen(
            viewModelFactory = searchViewModelFactory,
            onMoviePicked = { movieId ->
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("picked_movie_id", movieId)
                navController.popBackStack()
            },
            onBack = { navController.navigateUp() }
        )
    }
}