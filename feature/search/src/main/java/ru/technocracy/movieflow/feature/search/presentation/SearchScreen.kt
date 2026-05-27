package ru.technocracy.movieflow.feature.search.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.technocracy.movieflow.feature.search.di.SearchViewModelFactory

@Composable
fun SearchScreen(
    viewModelFactory: SearchViewModelFactory,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onQueryChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите запрос...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onBack) { Text("Назад") }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is SearchUiState.Idle -> Box(Modifier.fillMaxSize()) { Text("Начните вводить название фильма", modifier = Modifier.align(
                Alignment.Center)) }
            is SearchUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(32.dp))
            is SearchUiState.Error -> Text((uiState as SearchUiState.Error).message, color = MaterialTheme.colorScheme.error)
            is SearchUiState.Empty -> Text("Ничего не найдено", modifier = Modifier.align(Alignment.CenterHorizontally).padding(32.dp))
            is SearchUiState.Success -> LazyColumn {
                items((uiState as SearchUiState.Success).movies) { movie ->
                    TextButton(
                        onClick = { onMovieClick(movie.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("${movie.title} (${movie.year})") }
                }
            }
        }
    }
}