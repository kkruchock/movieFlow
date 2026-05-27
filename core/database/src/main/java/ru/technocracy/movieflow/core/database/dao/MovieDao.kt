package ru.technocracy.movieflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.technocracy.movieflow.core.database.entity.MovieEntity

@Dao
interface MovieDao {
    // одиночная вставка
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity)

    // множественная вставка
    @Insert(onConflict = OnConflictStrategy.REPLACE) // заменяем если уже есть
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies")
    suspend fun getAll(): List<MovieEntity>

    // удалить устаревшие
    @Query("DELETE FROM movies WHERE cachedAt < :expirationTime")
    suspend fun deleteExpired(expirationTime: Long)

    // фильм по id
    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): MovieEntity?
}