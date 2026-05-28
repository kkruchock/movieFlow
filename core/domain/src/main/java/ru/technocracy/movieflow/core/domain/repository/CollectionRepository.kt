package ru.technocracy.movieflow.core.domain.repository

import ru.technocracy.movieflow.core.domain.model.Collection

interface CollectionRepository {
    suspend fun createCollection(name: String, description: String?, isPublic: Boolean): Result<Collection>
    suspend fun updateCollection(collectionId: String, name: String, description: String?, isPublic: Boolean, movieIds: List<Int>): Result<Unit>
    suspend fun deleteCollection(collectionId: String): Result<Unit>
    suspend fun getCollection(collectionId: String): Result<Collection>
    suspend fun getUserCollections(userId: String): Result<List<Collection>>
    suspend fun addMovieToCollection(collectionId: String, movieId: Int): Result<Unit>
    suspend fun removeMovieFromCollection(collectionId: String, movieId: Int): Result<Unit>
    suspend fun reorderMoviesInCollection(collectionId: String, movieIds: List<Int>): Result<Unit>
    suspend fun getPublicCollections(): Result<List<Collection>>
}