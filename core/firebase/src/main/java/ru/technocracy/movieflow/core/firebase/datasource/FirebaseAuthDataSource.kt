package ru.technocracy.movieflow.core.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.technocracy.movieflow.core.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {

    companion object {
        private const val USERS_COLLECTION = "users"
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String): User {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = result.user ?: throw IllegalStateException("User is null after registration")

        val userDoc = hashMapOf(
            "email" to firebaseUser.email,
            "displayName" to firebaseUser.displayName,
            "photoUrl" to firebaseUser.photoUrl?.toString(),
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection(USERS_COLLECTION).document(firebaseUser.uid).set(userDoc).await()

        return firebaseUser.toDomain()
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): User? = auth.currentUser?.toDomain()

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun observeAuthState(): Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {firebaseAuth ->
            trySend(firebaseAuth.currentUser?.toDomain())
        }
        auth.addAuthStateListener(listener)
        trySend(auth.currentUser?.toDomain())
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private fun FirebaseUser.toDomain() = User(
        uid = uid,
        email = email.orEmpty(),
        displayName = displayName,
        photoUrl = photoUrl?.toString()
    )
}