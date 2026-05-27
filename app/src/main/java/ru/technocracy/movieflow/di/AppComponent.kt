package ru.technocracy.movieflow.di

import dagger.BindsInstance
import dagger.Component
import ru.technocracy.movieflow.core.database.di.DatabaseModule
import ru.technocracy.feature.feed.di.CatalogViewModelFactory
import ru.technocracy.movieflow.MovieFlowApplication
import ru.technocracy.movieflow.core.data.di.DataModule
import ru.technocracy.movieflow.core.firebase.di.FirebaseModule
import ru.technocracy.movieflow.core.network.di.NetworkModule
import ru.technocracy.movieflow.feature.auth.di.AuthModule
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import ru.technocracy.movieflow.feature.details.di.DetailsViewModelFactory
import javax.inject.Singleton

// связываем все модули
@Singleton
@Component(
    modules = [
        FirebaseModule::class,
        DataModule::class,
        AuthModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        AppModule::class,
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: MovieFlowApplication): AppComponent
    }

    fun inject(application: MovieFlowApplication)
    fun authViewModelFactory(): AuthViewModelFactory
    fun catalogViewModelFactory(): CatalogViewModelFactory
    fun detailsViewModelFactory(): DetailsViewModelFactory
}