package ru.technocracy.movieflow.feature.search.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.SearchMovieUseCase
import ru.technocracy.movieflow.feature.search.presentation.SearchViewModel
import javax.inject.Inject

class SearchViewModelFactory @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SearchViewModel(searchMovieUseCase) as T
}