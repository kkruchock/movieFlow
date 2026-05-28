package ru.technocracy.movieflow.core.domain.usecase.collection

import ru.technocracy.movieflow.core.domain.repository.CollectionRepository
import javax.inject.Inject

class RemoveMovieFromCollectionUseCase @Inject constructor(private val repo: CollectionRepository) {
    suspend operator fun invoke(collectionId: String, movieId: Int) = repo.removeMovieFromCollection(collectionId, movieId)
}