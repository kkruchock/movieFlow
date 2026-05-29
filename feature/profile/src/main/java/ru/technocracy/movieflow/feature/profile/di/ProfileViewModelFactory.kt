package ru.technocracy.movieflow.feature.profile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignOutUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetFavoriteMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetRatedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchlistMovieIdsUseCase
import ru.technocracy.movieflow.feature.profile.presentation.ProfileViewModel
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val signOut: SignOutUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getFavorites: GetFavoriteMovieIdsUseCase,
    private val getWatchlist: GetWatchlistMovieIdsUseCase,
    private val getWatched: GetWatchedMovieIdsUseCase,
    private val getRated: GetRatedMovieIdsUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ProfileViewModel(signOut, getCurrentUser, getFavorites, getWatchlist, getWatched, getRated) as T
}