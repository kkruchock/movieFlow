package ru.technocracy.movieflow.feature.auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.technocracy.movieflow.core.domain.usecase.IsLoggedInUseCase
import ru.technocracy.movieflow.core.domain.usecase.SignInUseCase
import ru.technocracy.movieflow.core.domain.usecase.SignUpUseCase
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