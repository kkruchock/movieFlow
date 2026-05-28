package ru.technocracy.movieflow.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.technocracy.movieflow.core.database.converter.ListConverters
import ru.technocracy.movieflow.core.database.dao.CollectionDao
import ru.technocracy.movieflow.core.database.dao.CollectionMovieDao
import ru.technocracy.movieflow.core.database.dao.MovieDao
import ru.technocracy.movieflow.core.database.dao.UserMovieDao
import ru.technocracy.movieflow.core.database.entity.CollectionEntity
import ru.technocracy.movieflow.core.database.entity.CollectionMovieEntity
import ru.technocracy.movieflow.core.database.entity.MovieEntity
import ru.technocracy.movieflow.core.database.entity.UserMovieEntity

@Database(
    entities = [
        MovieEntity::class,
        UserMovieEntity::class,
        CollectionEntity::class,
        CollectionMovieEntity::class,
               ],
    version = 3,
    exportSchema = false
)
@TypeConverters(
    ListConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun userMovieDao(): UserMovieDao
    abstract fun collectionDao(): CollectionDao
    abstract fun collectionMovieDao(): CollectionMovieDao
}