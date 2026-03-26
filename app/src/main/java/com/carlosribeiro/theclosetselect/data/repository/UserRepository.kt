package com.carlosribeiro.theclosetselect.data.repository

import com.carlosribeiro.theclosetselect.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun registerUser(user: User, password: String): Result<Unit> {
        return try {
            // 1. Criar no Auth
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = result.user?.uid ?: throw Exception("User ID not found")

            // 2. Salvar no Firestore com o mesmo UID
            db.collection("users")
                .document(uid)
                .set(user.copy(uid = uid))
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}