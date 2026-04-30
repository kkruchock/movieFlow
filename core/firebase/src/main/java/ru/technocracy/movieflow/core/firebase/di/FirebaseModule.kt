package ru.technocracy.movieflow.core.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton
import ru.technocracy.movieflow.core.firebase.datasource.FirebaseAuthDataSource

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
    ): FirebaseAuthDataSource = FirebaseAuthDataSource(auth, firestore)
}