package ru.technocracy.movieflow.feature.collections.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.collection.AddMovieToCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.CreateCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.GetCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.RemoveMovieFromCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.ReorderMoviesInCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.collection.UpdateCollectionUseCase
import ru.technocracy.movieflow.core.domain.usecase.movie.GetMovieDetailsUseCase
import ru.technocracy.movieflow.feature.collections.presentation.CollectionEditViewModel
import javax.inject.Inject

class CollectionsViewModelFactory @Inject constructor(
    private val createCollection: CreateCollectionUseCase,
    private val updateCollection: UpdateCollectionUseCase,
    private val getCollection: GetCollectionUseCase,
    private val addMovie: AddMovieToCollectionUseCase,
    private val removeMovie: RemoveMovieFromCollectionUseCase,
    private val reorderMovies: ReorderMoviesInCollectionUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase
) {
    fun create(collectionId: String?): ViewModelProvider.Factory =
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                CollectionEditViewModel(
                    collectionId = collectionId,
                    createCollection = createCollection,
                    updateCollection = updateCollection,
                    getCollection = getCollection,
                    addMovie = addMovie,
                    removeMovie = removeMovie,
                    reorderMovies = reorderMovies,
                    getMovieDetails = getMovieDetails
                ) as T
        }
}