package ru.technocracy.movieflow.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.technocracy.movieflow.core.database.AppDatabase
import ru.technocracy.movieflow.core.database.dao.CollectionDao
import ru.technocracy.movieflow.core.database.dao.CollectionMovieDao
import ru.technocracy.movieflow.core.database.dao.MovieDao
import ru.technocracy.movieflow.core.database.dao.UserMovieDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "movieflow_db") //todo хардкод
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()

    @Provides
    @Singleton
    fun provideUserMovieDao(db: AppDatabase): UserMovieDao = db.userMovieDao()

    @Provides
    @Singleton
    fun provideCollectionDao(db: AppDatabase): CollectionDao = db.collectionDao()

    @Provides
    @Singleton
    fun provideCollectionMovieDao(db: AppDatabase): CollectionMovieDao = db.collectionMovieDao()
}