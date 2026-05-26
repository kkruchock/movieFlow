package ru.technocracy.movieflow.di

import dagger.BindsInstance
import dagger.Component
import ru.technocracy.movieflow.MovieFlowApplication
import ru.technocracy.movieflow.core.data.di.DataModule
import ru.technocracy.movieflow.core.firebase.di.FirebaseModule
import ru.technocracy.movieflow.feature.auth.di.AuthModule
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import javax.inject.Singleton

// связываем все модули
@Singleton
@Component(
    modules = [
        FirebaseModule::class,
        DataModule::class,
        AuthModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: MovieFlowApplication): AppComponent
    }

    fun inject(application: MovieFlowApplication)
    fun authViewModelFactory(): AuthViewModelFactory
}