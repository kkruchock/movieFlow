package ru.technocracy.movieflow.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import ru.technocracy.movieflow.R
import ru.technocracy.movieflow.core.domain.model.User
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.technocracy.feature.feed.di.CatalogViewModelFactory
import ru.technocracy.feature.feed.presentation.CatalogScreen
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import ru.technocracy.movieflow.feature.auth.presentation.AuthScreen
import ru.technocracy.movieflow.feature.collections.di.CollectionsViewModelFactory
import ru.technocracy.movieflow.feature.collections.navigation.CollectionDetailRoute
import ru.technocracy.movieflow.feature.collections.navigation.CollectionEditRoute
import ru.technocracy.movieflow.feature.collections.navigation.CollectionsListRoute
import ru.technocracy.movieflow.feature.collections.navigation.collectionDetailScreen
import ru.technocracy.movieflow.feature.collections.navigation.collectionEditScreen
import ru.technocracy.movieflow.feature.collections.navigation.collectionsListScreen
import ru.technocracy.movieflow.feature.details.di.DetailsViewModelFactory
import ru.technocracy.movieflow.feature.details.navigation.DetailsRoute
import ru.technocracy.movieflow.feature.details.navigation.detailsScreen
import ru.technocracy.movieflow.feature.profile.di.ProfileViewModelFactory
import ru.technocracy.movieflow.feature.profile.di.UserMovieListViewModelFactory
import ru.technocracy.movieflow.feature.profile.navigation.UserMovieListRoute
import ru.technocracy.movieflow.feature.profile.navigation.userMovieListScreen
import ru.technocracy.movieflow.feature.profile.presentation.ProfileScreen
import ru.technocracy.movieflow.feature.search.di.SearchViewModelFactory
import ru.technocracy.movieflow.feature.search.navigation.searchPickScreen
import ru.technocracy.movieflow.feature.search.presentation.SearchScreen

object AppRoute {
    const val AUTH = "auth"
    const val HOME = "home"
    const val SEARCH = "search"
    const val PROFILE = "profile"
}

@Composable
fun AppNavGraph(
    isLoggedIn: Boolean,
    authStateFlow: Flow<User?>,
    authViewModelFactory: AuthViewModelFactory,
    catalogViewModelFactory: CatalogViewModelFactory,
    detailsViewModelFactory: DetailsViewModelFactory,
    searchViewModelFactory: SearchViewModelFactory,
    collectionsViewModelFactory: CollectionsViewModelFactory,
    profileViewModelFactory: ProfileViewModelFactory,
    userMovieListViewModelFactory: UserMovieListViewModelFactory,
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        authStateFlow
            .drop(1)
            .collect { user ->
                if (user == null) {
                    navController.navigate(AppRoute.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val tabRoutes = setOf(AppRoute.HOME, CollectionsListRoute.ROUTE, AppRoute.SEARCH, AppRoute.PROFILE)
    val showBottomBar = currentRoute in tabRoutes

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == AppRoute.HOME,
                        onClick = {
                            navController.navigate(AppRoute.HOME) {
                                popUpTo(AppRoute.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_home)) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == CollectionsListRoute.ROUTE,
                        onClick = {
                            navController.navigate(CollectionsListRoute.ROUTE) {
                                popUpTo(AppRoute.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.List, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_collections)) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == AppRoute.SEARCH,
                        onClick = {
                            navController.navigate(AppRoute.SEARCH) {
                                popUpTo(AppRoute.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Search, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_search)) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == AppRoute.PROFILE,
                        onClick = {
                            navController.navigate(AppRoute.PROFILE) {
                                popUpTo(AppRoute.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text(stringResource(R.string.nav_profile)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        val startDestination = if (isLoggedIn) AppRoute.HOME else AppRoute.AUTH

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppRoute.AUTH) {
                AuthScreen(
                    onNavigateToHome = {
                        navController.navigate(AppRoute.HOME) {
                            popUpTo(AppRoute.AUTH) { inclusive = true }
                        }
                    },
                    viewModelFactory = authViewModelFactory
                )
            }

            composable(AppRoute.HOME) {
                CatalogScreen(
                    viewModelFactory = catalogViewModelFactory,
                    onMovieClick = { movieId ->
                        navController.navigate(DetailsRoute.createRoute(movieId))
                    },
                    onCollectionClick = { collectionId ->
                        navController.navigate(CollectionDetailRoute.createRoute(collectionId))
                    },
                    onCreateCollection = {
                        navController.navigate(CollectionEditRoute.createRoute())
                    },
                    onSearchClick = {
                        navController.navigate(AppRoute.SEARCH) {
                            popUpTo(AppRoute.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(AppRoute.SEARCH) {
                SearchScreen(
                    viewModelFactory = searchViewModelFactory,
                    onMovieClick = { movieId ->
                        navController.navigate(DetailsRoute.createRoute(movieId))
                    },
                    onBack = null
                )
            }

            composable(AppRoute.PROFILE) {
                ProfileScreen(
                    viewModelFactory = profileViewModelFactory,
                    onLoggedOut = {
                        navController.navigate(AppRoute.AUTH) {
                            popUpTo(AppRoute.HOME) { inclusive = true }
                        }
                    },
                    onOpenList = { type ->
                        navController.navigate(UserMovieListRoute.createRoute(type))
                    }
                )
            }

            userMovieListScreen(
                viewModelFactory = userMovieListViewModelFactory,
                onMovieClick = { movieId ->
                    navController.navigate(DetailsRoute.createRoute(movieId))
                },
                onBack = { navController.navigateUp() }
            )

            collectionsListScreen(
                viewModelFactory = collectionsViewModelFactory,
                onCollectionClick = { collectionId ->
                    navController.navigate(CollectionDetailRoute.createRoute(collectionId))
                },
                onCreateCollection = {
                    navController.navigate(CollectionEditRoute.createRoute())
                }
            )

            collectionDetailScreen(
                viewModelFactory = collectionsViewModelFactory,
                onBack = { navController.navigateUp() },
                onEditCollection = { collectionId ->
                    navController.navigate(CollectionEditRoute.createRoute(collectionId))
                },
                onPickMovie = { navController.navigate("search_pick") }
            )

            detailsScreen(
                viewModelFactory = detailsViewModelFactory,
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
}