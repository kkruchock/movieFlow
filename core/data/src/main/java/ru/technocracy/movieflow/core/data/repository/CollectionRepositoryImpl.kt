package ru.technocracy.movieflow.core.data.repository

import ru.technocracy.movieflow.core.domain.model.Collection
import ru.technocracy.movieflow.core.data.mapper.toDomain
import ru.technocracy.movieflow.core.database.dao.CollectionDao
import ru.technocracy.movieflow.core.database.dao.CollectionMovieDao
import ru.technocracy.movieflow.core.database.entity.CollectionEntity
import ru.technocracy.movieflow.core.database.entity.CollectionMovieEntity
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import ru.technocracy.movieflow.core.domain.repository.CollectionRepository
import java.util.UUID
import javax.inject.Inject
import kotlin.collections.emptyList

class CollectionRepositoryImpl @Inject constructor(
    private val collectionDao: CollectionDao,
    private val collectionMovieDao: CollectionMovieDao,
    private val authRepository: AuthRepository
) : CollectionRepository {

    companion object {
        private const val PUBLIC_COLLECTIONS_LIMIT = 50
    }

    private val currentUserId: String
        get() = authRepository.getCurrentUser()?.uid ?: throw IllegalStateException("User not authenticated")

    override suspend fun createCollection(name: String, description: String?, isPublic: Boolean): Result<Collection> = runCatching {
        val now = System.currentTimeMillis()
        val entity = CollectionEntity(
            id = UUID.randomUUID().toString(),
            userId = currentUserId,
            name = name,
            description = description,
            isPublic = isPublic,
            createdAt = now,
            updatedAt = now
        )
        collectionDao.insert(entity)
        entity.toDomain(emptyList())
    }

    override suspend fun updateCollection(collectionId: String, name: String, description: String?, isPublic: Boolean, movieIds: List<Int>): Result<Unit> = runCatching {
        val entity = collectionDao.getById(collectionId)?.copy(
            name = name,
            description = description,
            isPublic = isPublic,
            updatedAt = System.currentTimeMillis()
        ) ?: throw IllegalArgumentException("Collection not found")
        collectionDao.update(entity)
        collectionMovieDao.reorderMovies(collectionId, movieIds)
    }

    override suspend fun deleteCollection(collectionId: String): Result<Unit> = runCatching {
        collectionDao.getById(collectionId)?.let {
            collectionDao.delete(it)
        }
        collectionMovieDao.deleteByCollectionId(collectionId)
    }

    override suspend fun getCollection(collectionId: String): Result<Collection> = runCatching {
        val entity = collectionDao.getById(collectionId) ?: throw IllegalArgumentException("Not found")
        val movieIds = collectionMovieDao.getMovieIds(collectionId)
        entity.toDomain(movieIds)
    }

    override suspend fun getUserCollections(userId: String): Result<List<Collection>> = runCatching {
        collectionDao.getByUserId(userId).map { entity ->
            val movieIds = collectionMovieDao.getMovieIds(entity.id)
            entity.toDomain(movieIds)
        }
    }

    override suspend fun addMovieToCollection(collectionId: String, movieId: Int): Result<Unit> = runCatching {
        if (collectionMovieDao.exists(collectionId, movieId)) return@runCatching
        val currentIds = collectionMovieDao.getMovieIds(collectionId)
        collectionMovieDao.insert(CollectionMovieEntity(collectionId, movieId, currentIds.size))
        val collection = collectionDao.getById(collectionId)
            ?: throw IllegalStateException("Collection $collectionId not found")
        collectionDao.update(collection.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun removeMovieFromCollection(collectionId: String, movieId: Int): Result<Unit> = runCatching {
        collectionMovieDao.delete(collectionId, movieId)
        val currentIds = collectionMovieDao.getMovieIds(collectionId)
        collectionMovieDao.reorderMovies(collectionId, currentIds)
        val collectionAfterRemove = collectionDao.getById(collectionId)
            ?: throw IllegalStateException("Collection $collectionId not found")
        collectionDao.update(collectionAfterRemove.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun reorderMoviesInCollection(collectionId: String, movieIds: List<Int>): Result<Unit> = runCatching {
        collectionMovieDao.reorderMovies(collectionId, movieIds)
        val collectionToReorder = collectionDao.getById(collectionId)
            ?: throw IllegalStateException("Collection $collectionId not found")
        collectionDao.update(collectionToReorder.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun getPublicCollections(): Result<List<Collection>> = runCatching {
        collectionDao.getPublicCollections(limit = PUBLIC_COLLECTIONS_LIMIT).map { entity ->
            val movieIds = collectionMovieDao.getMovieIds(entity.id)
            entity.toDomain(movieIds)
        }
    }
}