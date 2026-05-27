package ru.technocracy.movieflow.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.technocracy.movieflow.MovieFlowApplication
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app: MovieFlowApplication): Context = app.applicationContext
}