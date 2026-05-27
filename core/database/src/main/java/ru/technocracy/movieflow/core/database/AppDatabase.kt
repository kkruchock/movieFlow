package ru.technocracy.movieflow.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.technocracy.movieflow.core.database.converter.ListConverters
import ru.technocracy.movieflow.core.database.dao.MovieDao
import ru.technocracy.movieflow.core.database.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1, exportSchema = true
)
@TypeConverters(
    ListConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}