package ru.technocracy.movieflow.feature.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.Collection
import ru.technocracy.movieflow.feature.details.R
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.AddMovieToCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetUserCollectionsUseCase
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
    private val setRatingUseCase: SetRatingUseCase,
    private val getUserCollections: GetUserCollectionsUseCase,
    private val addMovieToCollection: AddMovieToCollectionUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    init { loadData() }

    private fun loadData() {
        viewModelScope.launch {
            val detailsResult = getMovieDetailsUseCase(movieId)
            if (detailsResult.isFailure) {
                val msg = detailsResult.exceptionOrNull()?.message
                    ?.let { UiText.Dynamic(it) }
                    ?: UiText.Resource(R.string.error_loading)
                _uiState.update { DetailsUiState.Error(msg) }
                return@launch
            }
            val details = detailsResult.getOrNull() ?: return@launch
            val action = getUserMovieActionUseCase(movieId).getOrNull()
            val collections = loadUserCollections()

            _uiState.update {
                DetailsUiState.Success(
                    details = details,
                    isFavorite = action?.isFavorite ?: false,
                    isPlanned = action?.isPlanned ?: false,
                    isWatched = action?.isWatched ?: false,
                    rating = action?.rating,
                    userCollections = collections
                )
            }
        }
    }

    private suspend fun loadUserCollections(): List<Collection> {
        val userId = getCurrentUser()?.uid ?: return emptyList()
        return getUserCollections(userId).getOrElse { emptyList() }
    }

    fun toggleFavorite() = refreshAfter { toggleFavoriteUseCase(movieId) }
    fun togglePlanned() = refreshAfter { togglePlannedUseCase(movieId) }
    fun toggleWatched() = refreshAfter { toggleWatchedUseCase(movieId) }

    fun setRating(value: Int) = refreshAfter { setRatingUseCase(movieId, value.coerceIn(1, 10)) }

    fun showCollectionsSheet() {
        viewModelScope.launch {
            val collections = loadUserCollections()
            _uiState.update { state ->
                (state as? DetailsUiState.Success)?.copy(
                    showCollectionsSheet = true,
                    userCollections = collections
                ) ?: state
            }
        }
    }

    fun dismissCollectionsSheet() {
        _uiState.update { (it as? DetailsUiState.Success)?.copy(showCollectionsSheet = false) ?: it }
    }

    fun addToCollection(collectionId: String) {
        viewModelScope.launch {
            addMovieToCollection(collectionId, movieId)
            val updated = loadUserCollections()
            _uiState.update { state ->
                (state as? DetailsUiState.Success)?.copy(
                    userCollections = updated,
                    showCollectionsSheet = false
                ) ?: state
            }
        }
    }

    fun showRatingDialog() {
        _uiState.update { (it as? DetailsUiState.Success)?.copy(showRatingDialog = true) ?: it }
    }

    fun dismissRatingDialog() {
        _uiState.update { (it as? DetailsUiState.Success)?.copy(showRatingDialog = false) ?: it }
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