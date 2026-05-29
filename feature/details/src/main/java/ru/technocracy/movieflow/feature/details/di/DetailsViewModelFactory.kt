package ru.technocracy.movieflow.feature.details.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.AddMovieToCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetUserCollectionsUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetUserMovieActionUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.SetRatingUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.ToggleFavoriteUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.TogglePlannedUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.ToggleWatchedUseCase
import ru.technocracy.movieflow.feature.details.presentation.DetailsViewModel
import javax.inject.Inject

class DetailsViewModelFactory @Inject constructor(
    private val getMovieDetails: GetMovieDetailsUseCase,
    private val getUserAction: GetUserMovieActionUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val togglePlanned: TogglePlannedUseCase,
    private val toggleWatched: ToggleWatchedUseCase,
    private val setRating: SetRatingUseCase,
    private val getUserCollections: GetUserCollectionsUseCase,
    private val addMovieToCollection: AddMovieToCollectionUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) {
    fun createWithId(movieId: Int): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DetailsViewModel(
                movieId,
                getMovieDetails,
                getUserAction,
                toggleFavorite,
                togglePlanned,
                toggleWatched,
                setRating,
                getUserCollections,
                addMovieToCollection,
                getCurrentUser
            ) as T
    }
}