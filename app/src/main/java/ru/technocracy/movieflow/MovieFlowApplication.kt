package ru.technocracy.movieflow

import android.app.Application
import com.google.firebase.FirebaseApp
import ru.technocracy.movieflow.di.AppComponent
import ru.technocracy.movieflow.di.DaggerAppComponent
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import javax.inject.Inject

class MovieFlowApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        appComponent.inject(this)
    }
}