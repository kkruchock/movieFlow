package ru.technocracy.movieflow.feature.collections.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.technocracy.movieflow.feature.collections.di.CollectionsViewModelFactory
import ru.technocracy.movieflow.feature.collections.presentation.CollectionDetailScreen
import ru.technocracy.movieflow.feature.collections.presentation.CollectionEditScreen
import ru.technocracy.movieflow.feature.collections.presentation.CollectionsListScreen

object CollectionEditRoute {
    const val ARG_COLLECTION_ID = "collectionId"
    const val ROUTE = "collection_edit?collectionId={$ARG_COLLECTION_ID}"
    fun createRoute(collectionId: String? = null): String =
        if (collectionId != null) "collection_edit?collectionId=$collectionId"
        else "collection_edit"
}

object CollectionsListRoute {
    const val ROUTE = "collections_list"
}

object CollectionDetailRoute {
    const val ARG_COLLECTION_ID = "collectionId"
    const val ROUTE = "collection_detail/{$ARG_COLLECTION_ID}"
    fun createRoute(collectionId: String) = "collection_detail/$collectionId"
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

fun NavGraphBuilder.collectionsListScreen(
    viewModelFactory: CollectionsViewModelFactory,
    onCollectionClick: (String) -> Unit,
    onCreateCollection: () -> Unit
) {
    composable(CollectionsListRoute.ROUTE) {
        CollectionsListScreen(
            viewModelFactory = viewModelFactory.createList(),
            onCollectionClick = onCollectionClick,
            onCreateCollection = onCreateCollection
        )
    }
}

fun NavGraphBuilder.collectionDetailScreen(
    viewModelFactory: CollectionsViewModelFactory,
    onBack: () -> Unit,
    onEditCollection: (String) -> Unit,
    onPickMovie: () -> Unit
) {
    composable(
        route = CollectionDetailRoute.ROUTE,
        arguments = listOf(
            navArgument(CollectionDetailRoute.ARG_COLLECTION_ID) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val collectionId = backStackEntry.arguments
            ?.getString(CollectionDetailRoute.ARG_COLLECTION_ID) ?: return@composable
        CollectionDetailScreen(
            collectionId = collectionId,
            viewModelFactory = viewModelFactory.createDetail(collectionId),
            onBack = onBack,
            onEditCollection = onEditCollection,
            onPickMovie = onPickMovie,
            navBackStackEntry = backStackEntry
        )
    }
}