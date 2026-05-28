package ru.technocracy.movieflow.feature.collections.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.technocracy.movieflow.feature.collections.di.CollectionsViewModelFactory
import ru.technocracy.movieflow.feature.collections.presentation.CollectionEditScreen

object CollectionEditRoute {
    const val ARG_COLLECTION_ID = "collectionId"
    const val ROUTE = "collection_edit?collectionId={$ARG_COLLECTION_ID}"

    fun createRoute(collectionId: String? = null): String =
        if (collectionId != null) "collection_edit?collectionId=$collectionId"
        else "collection_edit"
}

fun NavGraphBuilder.collectionEditScreen(
    viewModelFactory: CollectionsViewModelFactory,
    onBack: () -> Unit,
    onPickMovie: () -> Unit
) {
    composable(
        route = CollectionEditRoute.ROUTE,
        arguments = listOf(
            navArgument(CollectionEditRoute.ARG_COLLECTION_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val collectionId = backStackEntry.arguments
            ?.getString(CollectionEditRoute.ARG_COLLECTION_ID)
        CollectionEditScreen(
            viewModelFactory = viewModelFactory.create(collectionId),
            onBack = onBack,
            onPickMovie = onPickMovie,
            navBackStackEntry = backStackEntry
        )
    }
}