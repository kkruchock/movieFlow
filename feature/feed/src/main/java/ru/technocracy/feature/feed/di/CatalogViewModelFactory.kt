package ru.technocracy.feature.feed.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.feature.feed.presentation.CatalogViewModel
import ru.technocracy.movieflow.core.domain.usecase.movie.GetPopularMoviesUseCase
import javax.inject.Inject

class CatalogViewModelFactory @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CatalogViewModel(getPopularMoviesUseCase) as T
}