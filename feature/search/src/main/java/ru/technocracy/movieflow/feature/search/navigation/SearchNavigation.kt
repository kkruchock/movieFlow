package ru.technocracy.movieflow.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.technocracy.movieflow.feature.search.di.SearchViewModelFactory
import ru.technocracy.movieflow.feature.search.presentation.SearchScreen

object SearchRoute {
    const val ROUTE = "search"
}

fun NavGraphBuilder.searchScreen(
    viewModelFactory: SearchViewModelFactory,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    composable(SearchRoute.ROUTE) {
        SearchScreen(
            viewModelFactory = viewModelFactory,
            onMovieClick = onMovieClick,
            onBack = onBack
        )
    }
}

fun NavGraphBuilder.searchPickScreen(
    viewModelFactory: SearchViewModelFactory,
    onMoviePicked: (Int) -> Unit,
    onBack: () -> Unit
) {
    composable("search_pick") {
        SearchScreen(
            viewModelFactory = viewModelFactory,
            onMovieClick = onMoviePicked,
            onBack = onBack
        )
    }
}