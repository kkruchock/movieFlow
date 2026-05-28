package ru.technocracy.movieflow.feature.details.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
                val state = uiState as DetailsUiState.Success
                val details = state.details

                Text(text = details.title, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${details.year} • ★ ${details.rating} • ${details.runtime} мин")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = details.description ?: "Описание отсутствует")

                Spacer(modifier = Modifier.height(16.dp))
                Text("Действия:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { viewModel.toggleWatched() }) {
                        Text(if (state.isWatched) "Просмотрен ✓" else "Просмотрен")
                    }
                    TextButton(onClick = { viewModel.togglePlanned() }) {
                        Text(if (state.isPlanned) "В планах ✓" else "В планы")
                    }
                    TextButton(onClick = { viewModel.toggleFavorite() }) {
                        Text(if (state.isFavorite) "Избранное ♥" else "В избранное")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Оценка:", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(5, 7, 10).forEach { rating ->
                        TextButton(onClick = { viewModel.setRating(rating) }) {
                            Text(if (state.rating == rating) "★$rating" else "$rating")
                        }
                    }
                }
                if (state.rating != null) {
                    Text("Ваша оценка: ${state.rating}/10", modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}