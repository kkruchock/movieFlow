package ru.technocracy.movieflow.feature.details.presentation

import ru.technocracy.movieflow.core.domain.model.MovieDetails

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Success(val details: MovieDetails) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}