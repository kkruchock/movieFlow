package ru.technocracy.movieflow.core.domain.usecase.collection

import ru.technocracy.movieflow.core.domain.repository.CollectionRepository
import javax.inject.Inject

class CreateCollectionUseCase @Inject constructor(private val repo: CollectionRepository) {
    suspend operator fun invoke(name: String, description: String?, isPublic: Boolean) = repo.createCollection(name, description, isPublic)
}