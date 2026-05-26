package ru.technocracy.feature.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.domain.usecase.GetPopularMoviesUseCase
import javax.inject.Inject

class CatalogViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CatalogUiState>(CatalogUiState.Loading)
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init { loadMovies() }

    fun loadMovies() {
        viewModelScope.launch {
            _uiState.update { CatalogUiState.Loading }
            getPopularMoviesUseCase(1).fold(
                onSuccess = { movies -> _uiState.update { CatalogUiState.Success(movies) } },
                onFailure = { error -> _uiState.update { CatalogUiState.Error(error.localizedMessage ?: "Ошибка загрузки") } }
            )
        }
    }
}