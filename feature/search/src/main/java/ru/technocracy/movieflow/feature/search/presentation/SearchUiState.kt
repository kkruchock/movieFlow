package ru.technocracy.movieflow.feature.search.presentation

import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.Movie

sealed interface SearchUiState {
    data class Idle(val popularMovies: List<Movie> = emptyList()) : SearchUiState
    object Loading : SearchUiState
    data class Success(val movies: List<Movie>) : SearchUiState
    object Empty : SearchUiState
    data class Error(val message: UiText) : SearchUiState
}