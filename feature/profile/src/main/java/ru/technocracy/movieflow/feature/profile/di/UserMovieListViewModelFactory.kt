package ru.technocracy.movieflow.feature.profile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetFavoriteMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetRatedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchlistMovieIdsUseCase
import ru.technocracy.movieflow.feature.profile.presentation.UserMovieListViewModel
import javax.inject.Inject

class UserMovieListViewModelFactory @Inject constructor(
    private val getFavorites: GetFavoriteMovieIdsUseCase,
    private val getWatchlist: GetWatchlistMovieIdsUseCase,
    private val getWatched: GetWatchedMovieIdsUseCase,
    private val getRated: GetRatedMovieIdsUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase
) {
    fun createWithType(type: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            UserMovieListViewModel(type, getFavorites, getWatchlist, getWatched, getRated, getMovieDetails) as T
    }
}