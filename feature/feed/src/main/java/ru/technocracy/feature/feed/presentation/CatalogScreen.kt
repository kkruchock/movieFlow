package ru.technocracy.feature.feed.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.technocracy.feature.feed.di.CatalogViewModelFactory

// пока так, чисто посмотреть работоспособность
@Composable
fun CatalogScreen(
    viewModelFactory: CatalogViewModelFactory,
    viewModel: CatalogViewModel = viewModel(factory = viewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is CatalogUiState.Loading -> item {
                Spacer(modifier = Modifier.height(100.dp))
                CircularProgressIndicator()
            }
            is CatalogUiState.Error -> item {
                Text(text = (uiState as CatalogUiState.Error).message)
            }
            is CatalogUiState.Success -> items((uiState as CatalogUiState.Success).movies) { movie ->
                Text(
                    text = "${movie.title} (${movie.year})  ${movie.rating}",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

//todo ui +коррект when