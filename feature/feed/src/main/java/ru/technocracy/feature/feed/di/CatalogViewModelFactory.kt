package ru.technocracy.feature.feed.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.feature.feed.presentation.CatalogViewModel
import ru.technocracy.movieflow.core.domain.usecase.auth.GetCurrentUserUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetUserCollectionsUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetPopularMoviesUseCase
import javax.inject.Inject

class CatalogViewModelFactory @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val getUserCollections: GetUserCollectionsUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatalogViewModel(getPopularMovies, getUserCollections, getCurrentUser, getMovieDetails) as T
}