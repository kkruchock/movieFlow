package ru.technocracy.movieflow.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignOutUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetFavoriteMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetRatedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchedMovieIdsUseCase
import ru.technocracy.movieflow.core.domain.usecase.userAction.GetWatchlistMovieIdsUseCase

class ProfileViewModel(
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getFavorites: GetFavoriteMovieIdsUseCase,
    private val getWatchlist: GetWatchlistMovieIdsUseCase,
    private val getWatched: GetWatchedMovieIdsUseCase,
    private val getRated: GetRatedMovieIdsUseCase
) : ViewModel() {

    private val _loggedOut = MutableStateFlow(false)
    val loggedOut: StateFlow<Boolean> = _loggedOut.asStateFlow()

    val email: String = getCurrentUser()?.email.orEmpty()

    private val _counts = MutableStateFlow(ProfileCounts())
    val counts: StateFlow<ProfileCounts> = _counts.asStateFlow()

    init { loadCounts() }

    fun refresh() { loadCounts() }

    private fun loadCounts() {
        viewModelScope.launch {
            _counts.update {
                ProfileCounts(
                    favorites = getFavorites().getOrElse { emptyList() }.size,
                    watchlist = getWatchlist().getOrElse { emptyList() }.size,
                    watched = getWatched().getOrElse { emptyList() }.size,
                    rated = getRated().getOrElse { emptyList() }.size
                )
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase().onSuccess { _loggedOut.value = true }
        }
    }
}

data class ProfileCounts(
    val favorites: Int = 0,
    val watchlist: Int = 0,
    val watched: Int = 0,
    val rated: Int = 0
)