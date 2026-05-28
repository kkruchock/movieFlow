package ru.technocracy.movieflow.core.domain.usecase.collection

import ru.technocracy.movieflow.core.domain.repository.CollectionRepository
import javax.inject.Inject

class GetUserCollectionsUseCase @Inject constructor(private val repo: CollectionRepository) {
    suspend operator fun invoke(userId: String) = repo.getUserCollections(userId)
}