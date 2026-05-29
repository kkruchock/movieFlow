package ru.technocracy.movieflow.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.technocracy.core.ui.theme.MovieFlowTheme
import ru.technocracy.movieflow.MovieFlowApplication
import ru.technocracy.movieflow.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as MovieFlowApplication
        setContent {
            MovieFlowTheme {
                AppNavGraph(
                    isLoggedIn = app.isLoggedInUseCase(),
                    authStateFlow = app.authRepository.observeAuthState(),
                    authViewModelFactory = app.authViewModelFactory,
                    catalogViewModelFactory = app.catalogViewModelFactory,
                    detailsViewModelFactory = app.detailsViewModelFactory,
                    searchViewModelFactory = app.searchViewModelFactory,
                    collectionsViewModelFactory = app.collectionsViewModelFactory,
                    profileViewModelFactory = app.profileViewModelFactory,
                    userMovieListViewModelFactory = app.userMovieListViewModelFactory
                )
            }
        }
    }
}