package ru.technocracy.movieflow.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.domain.model.MovieDetails
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetFavoriteMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetRatedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchlistMovieIdsUseCase

class UserMovieListViewModel(
    private val type: String,
    private val getFavorites: GetFavoriteMovieIdsUseCase,
    private val getWatchlist: GetWatchlistMovieIdsUseCase,
    private val getWatched: GetWatchedMovieIdsUseCase,
    private val getRated: GetRatedMovieIdsUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserMovieListUiState())
    val uiState: StateFlow<UserMovieListUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val ids = when (type) {
                "favorites" -> getFavorites().getOrElse { emptyList() }
                "watchlist" -> getWatchlist().getOrElse { emptyList() }
                "watched"   -> getWatched().getOrElse { emptyList() }
                "rated"     -> getRated().getOrElse { emptyList() }
                else        -> emptyList()
            }
            val movies = ids.map { id ->
                async { getMovieDetails(id).getOrNull() }
            }.awaitAll().filterNotNull()
            _uiState.update { it.copy(isLoading = false, movies = movies) }
        }
    }
}

data class UserMovieListUiState(
    val isLoading: Boolean = true,
    val movies: List<MovieDetails> = emptyList()
)