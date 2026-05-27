package ru.technocracy.movieflow.feature.auth.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.domain.model.AuthError
import ru.technocracy.movieflow.core.domain.usecase.auth.IsLoggedInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignUpUseCase
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    //при создании проверяем авторизован ли пользователь
    init {
        if (isLoggedInUseCase()) {
            _uiState.update { AuthUiState.NavigateToHome }
        }
    }

    fun signIn(email: String, password: String) {
        if (!validateInput(email, password)) return
        executeAuthAction { signInUseCase(email, password) }
    }

    fun signUp(email: String, password: String) {
        if (!validateInput(email, password)) return
        executeAuthAction { signUpUseCase(email, password) }
    }

    fun onNavigationHandled() {
        _uiState.update { AuthUiState.Idle }
    }

    private fun validateInput(email: String, password: String): Boolean {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        return when {
            !isEmailValid -> {
                _uiState.update { AuthUiState.Error("Неверный формат email") }
                false
            }
            !isPasswordValid -> {
                _uiState.update { AuthUiState.Error("Пароль должен содержать минимум 6 символов") }
                false
            }
            else -> true
        }
    }

    private fun executeAuthAction(action: suspend () -> Result<*>) {
        _uiState.update { AuthUiState.Loading }
        viewModelScope.launch {
            action().fold(
                onSuccess = { _uiState.update { AuthUiState.NavigateToHome } },
                onFailure = { exception ->
                    _uiState.update { AuthUiState.Error(mapFirebaseError(exception)) }
                }
            )
        }
    }

    private fun mapFirebaseError(throwable: Throwable): String {
        return when (throwable) {
            is AuthError.WeakPassword -> "Пароль слишком простой"
            is AuthError.InvalidCredentials -> "Неверный email или пароль"
            is AuthError.UserAlreadyExists -> "Пользователь с таким email уже зарегистрирован"
            else -> throwable.localizedMessage ?: "Ошибка сети. Попробуйте позже"
        }
    }
}

//todo убрать хардкод
//todo побить на утилиты