package ru.technocracy.movieflow.feature.auth.presentation

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.technocracy.movieflow.core.ui.UiText
import ru.technocracy.movieflow.core.domain.model.AuthError
import ru.technocracy.movieflow.core.domain.usecase.auth.IsLoggedInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignInUseCase
import ru.technocracy.movieflow.core.domain.usecase.auth.SignUpUseCase
import ru.technocracy.movieflow.feature.auth.R
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }

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
        val isPasswordValid = password.length >= MIN_PASSWORD_LENGTH

        return when {
            !isEmailValid -> {
                _uiState.update { AuthUiState.Error(UiText.Resource(R.string.error_invalid_email)) }
                false
            }
            !isPasswordValid -> {
                _uiState.update { AuthUiState.Error(UiText.Resource(R.string.error_weak_password)) }
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
                    _uiState.update { AuthUiState.Error(mapAuthError(exception)) }
                }
            )
        }
    }

    private fun mapAuthError(throwable: Throwable): UiText = when (throwable) {
        is AuthError.WeakPassword -> UiText.Resource(R.string.error_weak_password)
        is AuthError.InvalidCredentials -> UiText.Resource(R.string.error_invalid_credentials)
        is AuthError.UserAlreadyExists -> UiText.Resource(R.string.error_user_exists)
        else -> throwable.localizedMessage
            ?.let { UiText.Dynamic(it) }
            ?: UiText.Resource(R.string.error_network)
    }
}
