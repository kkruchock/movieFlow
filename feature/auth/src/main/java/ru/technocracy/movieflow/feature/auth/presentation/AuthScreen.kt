package ru.technocracy.movieflow.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory

@Composable
fun AuthScreen(
    onNavigateToHome: () -> Unit,
    viewModelFactory: AuthViewModelFactory,
    viewModel: AuthViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoginMode by remember { mutableStateOf(true) }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.NavigateToHome) {
            onNavigateToHome()
            viewModel.onNavigationHandled()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(if (isLoginMode) "Вход" else "Регистрация")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isLoginMode) viewModel.signIn(email, password)
                else viewModel.signUp(email, password)
            },
            enabled = uiState !is AuthUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is AuthUiState.Loading) CircularProgressIndicator(modifier = Modifier.size(20.dp))
            else Text(if (isLoginMode) "Войти" else "Зарегистрироваться")
        }

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(if (isLoginMode) "Нет аккаунта? Регистрация" else "Уже есть аккаунт? Войти")
        }

        if (uiState is AuthUiState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = (uiState as AuthUiState.Error).message, color = MaterialTheme.colorScheme.error)
        }
    }
}
//todo хардкод
//todo ui