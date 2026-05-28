package ru.technocracy.movieflow.feature.details.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    private val setRating: SetRatingUseCase
) : ViewModelProvider.Factory {

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
                setRating
            ) as T
    }
}