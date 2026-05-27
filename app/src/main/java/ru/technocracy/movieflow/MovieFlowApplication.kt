package ru.technocracy.movieflow

import android.app.Application
import com.google.firebase.FirebaseApp
import ru.technocracy.feature.feed.di.CatalogViewModelFactory
import ru.technocracy.movieflow.di.AppComponent
import ru.technocracy.movieflow.di.DaggerAppComponent
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import ru.technocracy.movieflow.feature.details.di.DetailsViewModelFactory
import javax.inject.Inject

class MovieFlowApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory
    @Inject
    lateinit var catalogViewModelFactory: CatalogViewModelFactory
    @Inject lateinit var detailsViewModelFactory: DetailsViewModelFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        appComponent.inject(this)
    }
}