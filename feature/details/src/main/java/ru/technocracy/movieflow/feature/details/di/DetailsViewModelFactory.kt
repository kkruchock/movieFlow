package ru.technocracy.movieflow.feature.details.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.GetMovieDetailsUseCase
import ru.technocracy.movieflow.feature.details.presentation.DetailsViewModel
import javax.inject.Inject

class DetailsViewModelFactory @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModelProvider.Factory {

    fun createWithId(movieId: Int): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DetailsViewModel(movieId, getMovieDetailsUseCase) as T
    }
}