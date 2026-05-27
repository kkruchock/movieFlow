package ru.technocracy.movieflow.feature.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase

class DetailsViewModel(
    private val movieId: Int,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init { loadDetails() }

    private fun loadDetails() {
        viewModelScope.launch {
            _uiState.update { DetailsUiState.Loading }
            getMovieDetailsUseCase(movieId).fold(
                onSuccess = { details -> _uiState.update { DetailsUiState.Success(details) } },
                onFailure = { error -> _uiState.update { DetailsUiState.Error(error.localizedMessage ?: "Ошибка загрузки") } }
            )
        }
    }
}