package ru.technocracy.feature.feed.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import ru.technocracy.movieflow.core.domain.model.Collection
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetUserCollectionsUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetPopularMoviesUseCase
import javax.inject.Inject

class CatalogViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val getUserCollections: GetUserCollectionsUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            if (_uiState.value.popularMovies.isEmpty()) _uiState.update { it.copy(isLoading = true) }
            _uiState.update { it.copy(error = null) }

            supervisorScope {
                val userId = getCurrentUser()?.uid
                val moviesDeferred = async { getPopularMovies() }
                val collectionsDeferred = async {
                    if (userId != null) getUserCollections(userId)
                    else Result.success(emptyList<Collection>())
                }

                moviesDeferred.await()
                    .onSuccess { movies -> _uiState.update { it.copy(popularMovies = movies) } }
                    .onFailure { e -> _uiState.update { it.copy(error = e.message) } }

                val collections = collectionsDeferred.await().getOrElse { emptyList() }

                val posterDeferreds = collections.map { collection ->
                    collection.id to async {
                        collection.movieIds.firstOrNull()?.let { movieId ->
                            getMovieDetails(movieId).getOrNull()?.posterUrl
                        }
                    }
                }
                val posters = posterDeferreds.associate { (id, d) -> id to d.await() }

                _uiState.update {
                    it.copy(userCollections = collections, collectionPosters = posters, isLoading = false)
                }
            }
        }
    }
}