package ru.technocracy.movieflow.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.technocracy.movieflow.MovieFlowApplication
import ru.technocracy.movieflow.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as MovieFlowApplication

        setContent {
            AppNavGraph(
                authViewModelFactory = app.authViewModelFactory,
                catalogViewModelFactory = app.catalogViewModelFactory,
            )
        }
    }
}