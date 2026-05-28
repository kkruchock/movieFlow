package ru.technocracy.movieflow.core.domain.usecase.collection

import ru.technocracy.movieflow.core.domain.repository.CollectionRepository
import javax.inject.Inject

class UpdateCollectionUseCase @Inject constructor(private val repo: CollectionRepository) {
    suspend operator fun invoke(id: String, name: String, desc: String?, isPublic: Boolean, movies: List<Int>) =
        repo.updateCollection(id, name, desc, isPublic, movies)
}