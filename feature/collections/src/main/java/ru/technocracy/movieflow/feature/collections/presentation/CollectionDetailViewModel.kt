package ru.technocracy.movieflow.feature.collections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.MovieDetails
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.feature.collections.R
import ru.technocracy.movieflow.core.domain.usecase.collection.AddMovieToCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.DeleteCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.RemoveMovieFromCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.ReorderMoviesInCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase

class CollectionDetailViewModel(
    private val collectionId: String,
    private val getCollection: GetCollectionUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase,
    private val deleteCollection: DeleteCollectionUseCase,
    private val addMovie: AddMovieToCollectionUseCase,
    private val removeMovie: RemoveMovieFromCollectionUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val reorderMovies: ReorderMoviesInCollectionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionDetailUiState())
    val uiState: StateFlow<CollectionDetailUiState> = _uiState.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getCollection(collectionId).fold(
                onSuccess = { collection ->
                    val currentUserId = getCurrentUser()?.uid
                    val movies = coroutineScope {
                        collection.movieIds
                            .map { id -> async { getMovieDetails(id).getOrNull() } }
                            .awaitAll()
                    }.filterNotNull()
                    _uiState.update {
                        it.copy(
                            collection = collection,
                            movies = movies,
                            isOwnCollection = collection.userId == currentUserId,
                            isLoading = false
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = (e.localizedMessage?.let { UiText.Dynamic(it) }
                                ?: UiText.Resource(ru.technocracy.movieflow.core.ui.R.string.error_loading)).toString()
                        )
                    }
                }
            )
        }
    }

    fun onMovieAdded(movieId: Int) {
        viewModelScope.launch {
            if (_uiState.value.movies.any { it.id == movieId }) return@launch
            addMovie(collectionId, movieId).onSuccess {
                getMovieDetails(movieId).getOrNull()?.let { details ->
                    _uiState.update { it.copy(movies = it.movies + details) }
                }
            }
        }
    }

    fun onMovieRemoved(movieId: Int) {
        viewModelScope.launch {
            removeMovie(collectionId, movieId).onSuccess {
                _uiState.update { it.copy(movies = it.movies.filter { m -> m.id != movieId }) }
            }
        }
    }

    fun toggleShowAllMovies() {
        _uiState.update { it.copy(showAllMovies = !it.showAllMovies) }
    }

    fun showDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = true) }
    }

    fun dismissDeleteConfirm() {
        _uiState.update { it.copy(showDeleteConfirm = false) }
    }

    fun delete() {
        viewModelScope.launch {
            deleteCollection(collectionId)
                .onSuccess {
                    _uiState.update { it.copy(deleted = true, showDeleteConfirm = false) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            error = (e.localizedMessage?.let { UiText.Dynamic(it) }
                                ?: UiText.Resource(R.string.error_delete)).toString(),
                            showDeleteConfirm = false
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onMoviesReordered(movies: List<MovieDetails>) {
        _uiState.update { it.copy(movies = movies) }
        viewModelScope.launch {
            reorderMovies(collectionId, movies.map { it.id })
        }
    }
}