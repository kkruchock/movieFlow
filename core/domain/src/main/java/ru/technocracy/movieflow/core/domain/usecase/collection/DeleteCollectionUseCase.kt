package ru.technocracy.movieflow.core.domain.usecase.collection

import ru.technocracy.movieflow.core.domain.repository.CollectionRepository
import javax.inject.Inject

class DeleteCollectionUseCase @Inject constructor(private val repo: CollectionRepository) {
    suspend operator fun invoke(collectionId: String) = repo.deleteCollection(collectionId)
}