package ru.technocracy.movieflow.feature.auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.auth.IsLoggedInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignUpUseCase
import ru.technocracy.movieflow.feature.auth.presentation.AuthViewModel
import javax.inject.Inject

class AuthViewModelFactory @Inject constructor(
    private val signIn: SignInUseCase,
    private val signUp: SignUpUseCase,
    private val isLoggedIn: IsLoggedInUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AuthViewModel(signIn, signUp, isLoggedIn) as T
}