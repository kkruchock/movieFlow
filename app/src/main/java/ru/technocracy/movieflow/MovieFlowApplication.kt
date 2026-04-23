package ru.technocracy.movieflow

import android.app.Application
import com.google.firebase.FirebaseApp

class MovieFlowApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}