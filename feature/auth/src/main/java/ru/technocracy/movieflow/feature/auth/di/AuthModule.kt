package ru.technocracy.movieflow.feature.auth.di

import dagger.Module
import dagger.Provides
import ru.technocracy.movieflow.core.domain.usecase.auth.IsLoggedInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignUpUseCase

// для фабрики
@Module
class AuthModule {
    @Provides
    fun provideAuthViewModelFactory(
        signIn: SignInUseCase,
        signUp: SignUpUseCase,
        isLoggedIn: IsLoggedInUseCase
    ): AuthViewModelFactory = AuthViewModelFactory(signIn, signUp, isLoggedIn)
}