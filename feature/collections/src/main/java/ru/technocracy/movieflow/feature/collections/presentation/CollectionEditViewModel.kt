package ru.technocracy.movieflow.feature.collections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.MovieDetails
import ru.technocracy.movieflow.feature.collections.R
import ru.technocracy.movieflow.core.domain.usecase.collection.AddMovieToCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.CreateCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.RemoveMovieFromCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.ReorderMoviesInCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.UpdateCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase

class CollectionEditViewModel(
    private val collectionId: String?,
    private val createCollection: CreateCollectionUseCase,
    private val updateCollection: UpdateCollectionUseCase,
    private val getCollection: GetCollectionUseCase,
    private val addMovie: AddMovieToCollectionUseCase,
    private val removeMovie: RemoveMovieFromCollectionUseCase,
    private val reorderMovies: ReorderMoviesInCollectionUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionEditUiState())
    val uiState: StateFlow<CollectionEditUiState> = _uiState.asStateFlow()

    private var savedCollectionId: String? = collectionId

    init {
        if (collectionId != null) {
            _uiState.update { it.copy(isEditMode = true) }
            loadCollection(collectionId)
        }
    }

    private fun loadCollection(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getCollection(id)
                .onSuccess { collection ->
                    val movies = collection.movieIds.mapNotNull { movieId ->
                        getMovieDetails(movieId).getOrNull()
                    }
                    _uiState.update {
                        it.copy(
                            name = collection.name,
                            description = collection.description.orEmpty(),
                            isPublic = collection.isPublic,
                            movies = movies,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message as UiText?) }
                }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }

    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }

    fun onPublicToggle(value: Boolean) = _uiState.update { it.copy(isPublic = value) }

    fun onMovieAdded(movieId: Int) {
        viewModelScope.launch {
            if (_uiState.value.movies.any { it.id == movieId }) return@launch
            getMovieDetails(movieId).getOrNull()?.let { details ->
                _uiState.update { it.copy(movies = it.movies + details) }
            }
            val currentId = savedCollectionId ?: return@launch
            addMovie(currentId, movieId).onFailure { error ->
                _uiState.update { it.copy(error = error.message as UiText?) }
            }
        }
    }

    fun onMovieRemoved(movieId: Int) {
        _uiState.update { it.copy(movies = it.movies.filter { m -> m.id != movieId }) }
        val currentId = savedCollectionId ?: return
        viewModelScope.launch {
            removeMovie(currentId, movieId).onFailure { error ->
                _uiState.update { it.copy(error = error.message as UiText?) }
            }
        }
    }

    fun onMoviesReordered(movies: List<MovieDetails>) {
        _uiState.update { it.copy(movies = movies) }
        val currentId = savedCollectionId ?: return
        viewModelScope.launch {
            reorderMovies(currentId, movies.map { it.id })
        }
    }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) {
            _uiState.update { it.copy(error = UiText.Resource(R.string.error_collection_name_blank)) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val currentId = savedCollectionId
            if (currentId == null) {
                createCollection(
                    state.name.trim(),
                    state.description.trim().ifEmpty { null },
                    state.isPublic
                ).onSuccess { collection ->
                    savedCollectionId = collection.id
                    if (state.movies.isNotEmpty()) {
                        updateCollection(
                            collection.id,
                            state.name.trim(),
                            state.description.trim().ifEmpty { null },
                            state.isPublic,
                            state.movies.map { it.id }
                        ).onFailure { error ->
                            _uiState.update { it.copy(isSaving = false, error = error.message as UiText?) }
                            return@launch
                        }
                    }
                    _uiState.update { it.copy(isSaving = false, saved = true) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isSaving = false, error = error.message as UiText?) }
                }
            } else {
                updateCollection(
                    currentId,
                    state.name.trim(),
                    state.description.trim().ifEmpty { null },
                    state.isPublic,
                    state.movies.map { it.id }
                ).onSuccess {
                    _uiState.update { it.copy(isSaving = false, saved = true) }
                }.onFailure { error ->
                    _uiState.update { it.copy(isSaving = false, error = error.message as UiText?) }
                }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
}