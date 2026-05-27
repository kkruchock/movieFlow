package ru.technocracy.movieflow.feature.details.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.technocracy.movieflow.feature.details.di.DetailsViewModelFactory

@Composable
fun DetailsScreen(
    movieId: Int,
    viewModelFactory: DetailsViewModelFactory,
    onBack: () -> Unit,
    viewModel: DetailsViewModel = viewModel(factory = viewModelFactory.createWithId(movieId))
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TextButton(onClick = onBack) { Text("Назад") }
        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is DetailsUiState.Loading -> CircularProgressIndicator()
            is DetailsUiState.Error -> Text(text = (uiState as DetailsUiState.Error).message)
            is DetailsUiState.Success -> {
                val details = (uiState as DetailsUiState.Success).details
                Text(text = details.title, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${details.year} • ★ ${details.rating} • ${details.runtime} мин")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = details.description ?: "Описание отсутствует")
            }
        }
    }
}