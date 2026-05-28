package ru.technocracy.movieflow.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.technocracy.movieflow.core.database.entity.CollectionEntity

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(collection: CollectionEntity)
    @Query("SELECT * FROM collections WHERE userId = :userId ORDER BY updatedAt DESC")
    suspend fun getByUserId(userId: String): List<CollectionEntity>
    @Query("SELECT * FROM collections WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CollectionEntity?
    @Delete
    suspend fun delete(collection: CollectionEntity)
    @Query("UPDATE collections SET synced = true WHERE id = :id")
    suspend fun markAsSynced(id: String)
    @Update
    suspend fun update(collection: CollectionEntity)
    @Query("SELECT * FROM collections WHERE isPublic = true ORDER BY updatedAt DESC LIMIT :limit")
    suspend fun getPublicCollections(limit: Int = 20): List<CollectionEntity>
}