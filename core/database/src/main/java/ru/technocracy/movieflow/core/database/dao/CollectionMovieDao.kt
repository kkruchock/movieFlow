package ru.technocracy.movieflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.technocracy.movieflow.core.database.entity.CollectionMovieEntity

@Dao
interface CollectionMovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(relation: CollectionMovieEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(relations: List<CollectionMovieEntity>)
    @Query("SELECT movieId FROM collection_movies WHERE collectionId = :collectionId ORDER BY `order` ASC")
    suspend fun getMovieIds(collectionId: String): List<Int>
    @Query("DELETE FROM collection_movies WHERE collectionId = :collectionId")
    suspend fun deleteByCollectionId(collectionId: String)
    @Query("DELETE FROM collection_movies WHERE collectionId = :collectionId AND movieId = :movieId")
    suspend fun delete(collectionId: String, movieId: Int)
    @Query("SELECT EXISTS(SELECT 1 FROM collection_movies WHERE collectionId = :collectionId AND movieId = :movieId)")
    suspend fun exists(collectionId: String, movieId: Int): Boolean

    @Transaction
    suspend fun reorderMovies(collectionId: String, movieIdsInOrder: List<Int>) {
        deleteByCollectionId(collectionId)
        insertAll(movieIdsInOrder.mapIndexed {
            index, movieId -> CollectionMovieEntity(collectionId, movieId, index)
        })
    }
}