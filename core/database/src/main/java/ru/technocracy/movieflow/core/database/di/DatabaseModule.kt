package ru.technocracy.movieflow.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.technocracy.movieflow.core.database.AppDatabase
import ru.technocracy.movieflow.core.database.dao.MovieDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "movieflow_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideMovieDao(db: AppDatabase): MovieDao = db.movieDao()
}