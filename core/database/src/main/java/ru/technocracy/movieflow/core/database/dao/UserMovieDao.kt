package ru.technocracy.movieflow.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.technocracy.movieflow.core.database.entity.UserMovieEntity

@Dao
interface UserMovieDao {
    // всталяем или обннолвяем сущность
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(action: UserMovieEntity)

    // получаем по юзер + фильм
    @Query("SELECT * FROM user_movies WHERE userId = :userId AND movieId = :movieId LIMIT 1")
    suspend fun getAction(userId: String, movieId: Int): UserMovieEntity?

    @Query("SELECT * FROM user_movies WHERE userId = :userId")
    suspend fun getAll(userId: String): List<UserMovieEntity>
}