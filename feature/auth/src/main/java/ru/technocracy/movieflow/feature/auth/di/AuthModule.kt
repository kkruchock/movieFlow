package ru.technocracy.movieflow.feature.auth.di

import dagger.Module
import dagger.Provides
import ru.technocracy.movieflow.core.domain.usecase.IsLoggedInUseCase
import ru.technocracy.movieflow.core.domain.usecase.SignInUseCase
import ru.technocracy.movieflow.core.domain.usecase.SignUpUseCase

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