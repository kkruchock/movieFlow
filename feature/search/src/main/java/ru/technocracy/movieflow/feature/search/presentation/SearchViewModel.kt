package ru.technocracy.movieflow.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.Movie
import ru.technocracy.movieflow.core.domain.usecase.movie.GetPopularMoviesUseCase
import ru.technocracy.movieflow.core.domain.usecase.search.SearchMovieUseCase
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase,
    private val getPopularMovies: GetPopularMoviesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var cachedPopular: List<Movie> = emptyList()

    init {
        loadPopularMovies()
        viewModelScope.launch {
            _query
                .debounce(300)
                .distinctUntilChanged()
                .flatMapLatest { q ->
                    flow<Result<List<Movie>>?> {
                        if (q.isBlank()) {
                            emit(null)
                        } else {
                            _uiState.update { SearchUiState.Loading }
                            emit(searchMovieUseCase(q))
                        }
                    }
                }
                .collect { result ->
                    if (result == null) {
                        _uiState.update { SearchUiState.Idle(cachedPopular) }
                    } else {
                        result.fold(
                            onSuccess = { movies ->
                                _uiState.update {
                                    if (movies.isEmpty()) SearchUiState.Empty
                                    else SearchUiState.Success(movies)
                                }
                            },
                            onFailure = { error ->
                                _uiState.update {
                                    SearchUiState.Error(
                                        error.message?.let { UiText.Dynamic(it) }
                                            ?: UiText.Resource(ru.technocracy.movieflow.core.ui.R.string.error_loading)
                                    )
                                }
                            }
                        )
                    }
                }
        }
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            getPopularMovies().onSuccess { movies ->
                cachedPopular = movies
                if (_query.value.isBlank()) {
                    _uiState.update { SearchUiState.Idle(movies) }
                }
            }
        }
    }

    fun onQueryChanged(query: String) { _query.value = query }

    fun onClear() {
        _query.value = ""
        _uiState.update { SearchUiState.Idle(cachedPopular) }
    }
}