package ru.technocracy.movieflow.feature.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetUserMovieActionUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.SetRatingUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.ToggleFavoriteUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.TogglePlannedUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.ToggleWatchedUseCase

class DetailsViewModel(
    private val movieId: Int,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getUserMovieActionUseCase: GetUserMovieActionUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val togglePlannedUseCase: TogglePlannedUseCase,
    private val toggleWatchedUseCase: ToggleWatchedUseCase,
    private val setRatingUseCase: SetRatingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            val detailsResult = getMovieDetailsUseCase(movieId)
            if (detailsResult.isFailure) {
                _uiState.update { DetailsUiState.Error(detailsResult.exceptionOrNull()?.message ?: "Ошибка загрузки") }
                return@launch
            }
            val details = detailsResult.getOrNull()!!

            getUserMovieActionUseCase(movieId).fold(
                onSuccess = { action ->
                    _uiState.update { DetailsUiState.Success(
                        details,
                        action.isFavorite,
                        action.isPlanned,
                        action.isWatched,
                        action.rating
                    ) }
                },
                onFailure = {
                    _uiState.update { DetailsUiState.Success(
                        details,
                        false,
                        false,
                        false,
                        null
                    ) }
                }
            )
        }
    }

    fun toggleFavorite() = refreshAfter {
        toggleFavoriteUseCase(movieId)
    }
    fun togglePlanned() = refreshAfter {
        togglePlannedUseCase(movieId)
    }
    fun toggleWatched() = refreshAfter {
        toggleWatchedUseCase(movieId)
    }
    fun setRating(value: Int) = refreshAfter {
        setRatingUseCase(movieId, value.coerceIn(1, 10))
    }

    private fun refreshAfter(action: suspend () -> Result<Unit>) {
        viewModelScope.launch {
            if (action().isSuccess) {
                getUserMovieActionUseCase(movieId).onSuccess { updated ->
                    _uiState.update { state ->
                        (state as? DetailsUiState.Success)?.copy(
                            isFavorite = updated.isFavorite,
                            isPlanned = updated.isPlanned,
                            isWatched = updated.isWatched,
                            rating = updated.rating
                        ) ?: state
                    }
                }
            }
        }
    }
}