package ru.technocracy.movieflow.core.data.di

import dagger.Binds
import dagger.Module
import ru.technocracy.movieflow.core.data.repository.AuthRepositoryImpl
import ru.technocracy.movieflow.core.data.repository.MovieRepositoryImpl
import ru.technocracy.movieflow.core.data.repository.UserDataRepositoryImpl
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import ru.technocracy.movieflow.core.domain.repository.MovieRepository
import ru.technocracy.movieflow.core.domain.repository.UserDataRepository

// подстановка интерфейса для dagger
@Module
abstract class DataModule{

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository

    @Binds abstract fun bindUserDataRepository(
        impl: UserDataRepositoryImpl
    ): UserDataRepository
}