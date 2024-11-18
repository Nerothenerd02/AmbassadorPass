package com.example.ambassadorpass.repository

import com.example.ambassadorpass.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.util.Log


class UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Authenticate the user with email and password
    suspend fun authenticate(email: String, password: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Authentication error: ${e.message}")
            false
        }
    }

    // Get user data from Firestore based on UID
    suspend fun getUserByEmail(email: String): User? {
        return try {
            Log.d("UserRepository", "Attempting to retrieve document for email: $email")
            val querySnapshot = firestore.collection("ambassadors")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val user = document.toObject(User::class.java)
                user?.apply {
                    tier = document.getString("tier")?.lowercase() ?: "unknown"
                }
                Log.d("UserRepository", "User data retrieved successfully: $user")
                user
            } else {
                Log.d("UserRepository", "No document found for email: $email")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error retrieving user data by email: ${e.message}", e)
            null
        }
    }

}

