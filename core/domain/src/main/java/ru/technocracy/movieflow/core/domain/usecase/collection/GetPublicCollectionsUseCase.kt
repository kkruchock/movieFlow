package ru.technocracy.movieflow.core.domain.usecase.collection

import ru.technocracy.movieflow.core.domain.repository.CollectionRepository
import javax.inject.Inject

class GetPublicCollectionsUseCase @Inject constructor(private val repo: CollectionRepository) {
    suspend operator fun invoke() = repo.getPublicCollections()
}