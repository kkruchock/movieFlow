package ru.technocracy.movieflow.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.domain.usecase.SearchMovieUseCase
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(300)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .flatMapLatest { searchQuery ->
                    flow {
                        emit(searchMovieUseCase(searchQuery))
                    }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { movies ->
                            _uiState.update {
                                if (movies.isEmpty()) SearchUiState.Empty
                                else SearchUiState.Success(movies)
                            }
                        },
                        onFailure = { _uiState.update {
                            SearchUiState.Error((it as? java.lang.Exception)?.message ?: "Ошибка загрузки")
                        } }
                    )
                }
        }
    }

    fun onQueryChanged(query: String) { _query.value = query }
    fun onClear() { _query.value = ""; _uiState.update { SearchUiState.Idle } }
}